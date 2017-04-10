package org.didinem.rpc.server;

import com.google.common.collect.Lists;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.collections4.CollectionUtils;
import org.didinem.rpc.ServiceProvider;
import org.didinem.rpc.ServiceProviderBuilder;
import org.didinem.rpc.annotation.RpcService;
import org.didinem.rpc.registry.ServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;

/**
 * Created by didinem on 4/2/2017.
 */
public class RpcServer implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private String registryIp;

    private String registryPort;

    private String servicePort;

    public String getRegistryIp() {
        return registryIp;
    }

    public void setRegistryIp(String registryIp) {
        this.registryIp = registryIp;
    }

    public String getRegistryPort() {
        return registryPort;
    }

    public void setRegistryPort(String registryPort) {
        this.registryPort = registryPort;
    }

    public String getServicePort() {
        return servicePort;
    }

    public void setServicePort(String servicePort) {
        this.servicePort = servicePort;
    }

    private List<Object> scanRpcServices() {
        Map<String, Object> maps = applicationContext.getBeansWithAnnotation(RpcService.class);
        List<Object> rpcServices = Lists.newArrayListWithCapacity(maps.size());
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            Object serviceObject = entry.getValue();
            rpcServices.add(serviceObject);
        }
        return rpcServices;
    }

    private void registerProviders(List<ServiceProvider> serviceProviders) {
        if (CollectionUtils.isEmpty(serviceProviders)) {
            return;
        }
        for (ServiceProvider serviceProvider : serviceProviders) {
            ServiceRegistry.register(serviceProvider);
        }
    }

    private void listeningRequest() {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingResolver(RpcServer.class.getClassLoader())));
                            ch.pipeline().addLast(new RpcServerChannelHandler());
                        }
                    });

            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(Integer.parseInt(servicePort)).sync();

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void afterPropertiesSet() throws Exception {
        List<Object> rpcServices = scanRpcServices();
        if (CollectionUtils.isEmpty(rpcServices)) {
            return;
        }
        List<ServiceProvider> serviceProviders = Lists.newArrayListWithCapacity(rpcServices.size());
        for (Object object : rpcServices) {
            String addr = InetAddress.getLocalHost().getHostAddress();
            ServiceProvider serviceProvider = ServiceProviderBuilder.build(addr, servicePort, object.getClass());
            serviceProviders.add(serviceProvider);
        }
        registerProviders(serviceProviders);

        listeningRequest();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
