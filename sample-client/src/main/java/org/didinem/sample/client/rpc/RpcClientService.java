package org.didinem.sample.client.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.didinem.sample.rpc.RpcInvocation;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutionException;

@Service
public class RpcClientService {

    private EventLoopGroup group;

    private Channel channel;

    public void initClient(String hostAddress, int port) {
        // 配置客户端NIO线程组
        group = new NioEventLoopGroup();
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
        ChannelFuture f = null;
        try {
            f = b.connect(hostAddress, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.channel = f.channel();
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


}
