package org.didinem.rpc.client;

import com.google.common.collect.Maps;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.didinem.rpc.RpcRequest;
import org.didinem.rpc.RpcResponse;
import org.didinem.transport.ResponseFuture;
import org.didinem.transport.client.NettyClient;

import java.util.Map;

/**
 * Created by didinem on 4/16/2017.
 */
public class RpcClient {

    private String host;

    private int port;

    private Channel channel;

    private NettyClient nettyClient;

    public static Map<String, ResponseFuture> responseFutureMap = Maps.newConcurrentMap();

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    public Channel getChannel() {
        return channel;
    }

    public void init() {
        // 配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingResolver(RpcClient.class.getClassLoader())));
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4));
                            ch.pipeline().addLast(new LengthFieldPrepender(4, false));
                            ch.pipeline().addLast(new ObjectEncoder());
                        }
                    });

            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();
            channel = f.channel();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }

    public RpcResponse request(RpcRequest rpcRequest) {
        ResponseFuture<RpcResponse> responseFuture = nettyClient.request(rpcRequest);
        responseFutureMap.put(rpcRequest.getRpcId(), responseFuture);
        RpcResponse rpcResponse = responseFuture.get();
        return rpcResponse;
    }

}
