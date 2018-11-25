package org.didinem.sample.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.didinem.sample.server.test.SampleServerInboundHandler;
import org.didinem.sample.server.test.ThreadPoolService;

/**
 * Created by didinem on 11/18/2018.
 */
public class RpcSampleServer {

    private String hostAddress;

    private int port;

    private NioServerSocketChannel serverSocketChannel;

    private EventLoopGroup group;

    public RpcSampleServer(String hostAddress, int port) {
        this.hostAddress = hostAddress;
        this.port = port;
    }

    public void bind() throws Exception {
        final ThreadPoolService threadPoolService = new ThreadPoolService();
        final RpcServerInboundHandler rpcServerInboundHandler = new RpcServerInboundHandler(threadPoolService);
        group = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(port)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws Exception {
                        ch.pipeline().addLast(rpcServerInboundHandler);
                    }
                });

        // 绑定端口，同步等待成功
        ChannelFuture f = b.bind().sync();
        serverSocketChannel = (NioServerSocketChannel) f.channel();
    }

    public void close() {
        serverSocketChannel.close();
        try {
            group.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
