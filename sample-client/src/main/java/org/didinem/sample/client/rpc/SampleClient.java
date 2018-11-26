package org.didinem.sample.client.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.didinem.sample.RpcInvocation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutionException;


/**
 * Created by didinem on 11/10/2018.
 */
public class SampleClient {

    private Channel channel;

    private EventLoopGroup group;

    public void connect(int port, String host) throws Exception {
        // 配置客户端NIO线程组
        this.group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ch.pipeline().addLast(new SampleClientHandler());
                    }
                });

        // 发起异步连接操作
        ChannelFuture f = b.connect(host, port).sync();
        this.channel = f.channel();
        System.out.println("创建channel：" + this.channel);
    }

    public ResponseFuture sendRequest(RpcInvocation rpcInvocation) {
        ResponseFuture responseFuture = new ResponseFuture();
        responseFuture.setRpcID(rpcInvocation.getRpcID());

        ResponseFutureMap.put(responseFuture.getRpcID(), responseFuture);

        byte[] objectByte = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(rpcInvocation);
            objectByte = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ChannelFuture channelFuture = this.channel.writeAndFlush(Unpooled.copiedBuffer(objectByte));
        responseFuture.setWriteFuture(channelFuture);
        try {
            channelFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            ResponseFutureMap.remove(responseFuture.getRpcID());
            return null;
        }

        return responseFuture;
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int port = 8080;
        SampleClient sampleClient = new SampleClient();
        sampleClient.connect(port, "127.0.0.1");

        try {
            RpcInvocation rpcInvocation = new RpcInvocation();
            rpcInvocation.setInterfaceQualifiedName("org.didinem.sample.service.TestService");
            rpcInvocation.setMethodName("test");
            rpcInvocation.setParametersTypes(new Class[]{String.class});
            rpcInvocation.setParameters(new Object[]{"abc"});

            ResponseFuture responseFuture = sampleClient.sendRequest(rpcInvocation);
            Object object = responseFuture.get();
            System.out.println(object);
            System.out.println(sampleClient.channel);
        } finally {
            sampleClient.channel.close().sync();
            sampleClient.group.shutdownGracefully().sync();
        }

    }

}
