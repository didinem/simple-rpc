package org.didinem.sample.server.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * Created by didinem on 11/10/2018.
 */
public class SampleServer {

    public void bind(int port) throws Exception {
        final ThreadPoolService threadPoolService = new ThreadPoolService();
        final SampleServerInboundHandler sampleServerInboundHandler = new SampleServerInboundHandler(threadPoolService);
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(sampleServerInboundHandler);
                        }
                    });

            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind().sync();

            Channel channel = f.channel();

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new SampleServer().bind(port);
    }

}
