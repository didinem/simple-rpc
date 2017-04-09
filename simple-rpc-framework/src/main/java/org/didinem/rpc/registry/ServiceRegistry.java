package org.didinem.rpc.registry;

import com.google.common.collect.Lists;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections4.CollectionUtils;
import org.didinem.rpc.ServiceProvider;
import org.didinem.rpc.ServiceProviderBuilder;

import java.util.List;

/**
 * Created by didinem on 4/2/2017.
 */
public class ServiceRegistry {

    private static ZkClient zkClient = new ZkClient("127.0.0.1:2181");

    private static final String ROOT_PATH = "/simple-rpc";

    private static final String SEPARATOR = "/";

    private static final String PROVIDER_PATH = "/simple-rpc/providers";

    static {
        if (!zkClient.exists(PROVIDER_PATH)) {
            zkClient.createPersistent(PROVIDER_PATH, true);
        }

    }

    public static void register(ServiceProvider serviceProvider) {
        // FIXME 生成nodeName的方式
        String interfaceNode = serviceProvider.getInterfaceClass().getName();
        String serviceNode = serviceProvider.getIp() + ":" + serviceProvider.getPort();
        String nodePath = PROVIDER_PATH + SEPARATOR + interfaceNode + SEPARATOR + serviceNode;
        zkClient.createPersistent(nodePath, true);
    }

    public static List<ServiceProvider> subscribe(Class interfaceClass) {
        List<ServiceProvider> serviceProviders = null;
        String interfaceNode = PROVIDER_PATH + SEPARATOR + interfaceClass.getName();
        if (zkClient.exists(interfaceNode)) {
            List<String> providers = zkClient.getChildren(interfaceNode);
            if (CollectionUtils.isNotEmpty(providers)) {
                serviceProviders = Lists.newArrayListWithCapacity(providers.size());
                for (String string : providers) {
                    String[] rpcProperty = string.split(":");
                    ServiceProvider serviceProvider = ServiceProviderBuilder.build(rpcProperty[0], rpcProperty[1], interfaceClass);
                    serviceProviders.add(serviceProvider);
                }
            }
        }
        return serviceProviders;
    }

}
