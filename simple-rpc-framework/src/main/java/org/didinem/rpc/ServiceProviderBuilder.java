package org.didinem.rpc;

/**
 * Created by didinem on 4/3/2017.
 */
public class ServiceProviderBuilder {

    public static ServiceProvider build(String ip, String port, String interfaceName) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setIp(ip);
        serviceProvider.setPort(port);
        serviceProvider.setInterfaceClass(interfaceName);
        return serviceProvider;
    }

}
