package org.didinem.rpc.client;

import io.netty.channel.Channel;
import org.didinem.rpc.RpcRequest;
import org.didinem.rpc.ServiceProvider;
import org.didinem.rpc.registry.ServiceRegistry;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by didinem on 4/3/2017.
 */
public class RpcServiceFactoryBean implements FactoryBean {

    private String registryIp;

    private String registryPort;

    private String interfaceClassName;

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

    public String getInterfaceClassName() {
        return interfaceClassName;
    }

    public void setInterfaceClassName(String interfaceClassName) {
        this.interfaceClassName = interfaceClassName;
    }

    public Object getObject() throws Exception {
        List<ServiceProvider> serviceProviders = ServiceRegistry.subscribe(interfaceClassName);
        if (null == serviceProviders) {
            return null;
        }
        ServiceProvider serviceProvider = null;
        if (serviceProviders.size() > 0) {
            serviceProvider = serviceProviders.get(0);
        }
        if (serviceProvider == null) {
            return null;
        }
        final RpcClient rpcClient = new RpcClient(serviceProvider.getIp(), Integer.valueOf(serviceProvider.getPort()));


        InvocationHandler rpcHandler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Channel channel = rpcClient.getChannel();
                RpcRequest rpcRequest = new RpcRequest(interfaceClassName, method.getName(), args);
                channel.writeAndFlush(rpcRequest);
                return "bb";
            }
        };
        Class interfaceClass = Class.forName(interfaceClassName);
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{interfaceClass}, rpcHandler);
    }

    public Class<?> getObjectType() {
        Class interfaceClass = Object.class;
        try {
            interfaceClass = Class.forName(interfaceClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return interfaceClass;
    }

    public boolean isSingleton() {
        return true;
    }
}
