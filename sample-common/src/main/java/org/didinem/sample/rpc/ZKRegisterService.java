package org.didinem.sample.rpc;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * Created by didinem on 11/18/2018.
 */
public class ZKRegisterService {

    private static final String FRAMEWORK_NAMESPACE = "simple-rpc";
    private static final String SEPARATOR = "/";
    private static final String PROVIDERS_ZNODE = "providers";

    public void register(ProviderInfo providerInfo) {

        String connectString = "192.168.33.111:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .retryPolicy(retryPolicy)
                .namespace(FRAMEWORK_NAMESPACE)
                .build();
        client.start();

        List<String> znodes = Lists.newArrayList("", providerInfo.getQualifiedClassName(), PROVIDERS_ZNODE, getProviderKey(providerInfo));
        try {
            client.create().creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(Joiner.on(SEPARATOR).join(znodes));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        client.close();
    }

    public static String getProviderKey(ProviderInfo providerInfo) {
        return providerInfo.getHostAddress() + ":" + providerInfo.getPort();
    }

    public List<ProviderInfo> listProviderInfo(Class providerInterfaceClass) {
        String connectString = "192.168.33.111:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .retryPolicy(retryPolicy)
                .namespace(FRAMEWORK_NAMESPACE)
                .build();
        client.start();

        String qualifiedInterfaceName = providerInterfaceClass.getName();

        List<String> childrenPaths = null;
        String path = Joiner.on(SEPARATOR).join(new String[]{qualifiedInterfaceName, PROVIDERS_ZNODE});

        try {
            childrenPaths = client.getChildren()
                    .forPath(SEPARATOR + Joiner.on(SEPARATOR).join(new String[]{qualifiedInterfaceName, PROVIDERS_ZNODE}));
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();

        if (childrenPaths == null || childrenPaths.size() == 0) {
            return Lists.newArrayListWithCapacity(0);
        }

        List<ProviderInfo> providerInfos = Lists.newArrayListWithCapacity(childrenPaths.size());
        for (String childrenPath : childrenPaths) {
            String[] elements = childrenPath.split("\\:");
            providerInfos.add(new ProviderInfo(providerInterfaceClass, elements[0], Integer.valueOf(elements[1])));
        }
        return providerInfos;
    }

    public static void main(String[] args) {
//        ZKRegisterService zkRegisterService = new ZKRegisterService();
//        ProviderInfo providerInfo = new ProviderInfo();
//        providerInfo.setProviderInterfaceClass(RpcInvocation.class);
//        providerInfo.setHostAddress("127.0.0.1");
//        providerInfo.setPort(20880);
//        zkRegisterService.register(providerInfo);
    }

}
