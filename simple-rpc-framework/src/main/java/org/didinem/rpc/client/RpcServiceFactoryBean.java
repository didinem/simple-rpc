package org.didinem.rpc.client;

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

    private Class interfaceClass;

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

    public Class getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Object getObject() throws Exception {
        List<ServiceProvider> serviceProviders = ServiceRegistry.subscribe(interfaceClass);
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


        InvocationHandler rpcHandler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                return "bb";
            }
        };

        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{interfaceClass}, rpcHandler);
    }

    public Class<?> getObjectType() {
        return interfaceClass;
    }

    public boolean isSingleton() {
        return true;
    }

}
