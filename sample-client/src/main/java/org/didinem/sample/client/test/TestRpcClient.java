package org.didinem.sample.client.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * Created by didinem on 11/25/2018.
 */
public class TestRpcClient {

    private static final String FRAMEWORK_NAMESPACE = "simple-rpc";
    private static final String SEPARATOR = "/";
    private static final String PROVIDERS_ZNODE = "providers";

    public static void main(String[] args) {
        String connectString = "192.168.33.111:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .retryPolicy(retryPolicy)
                .namespace(FRAMEWORK_NAMESPACE)
                .build();
        client.start();

        List<String> strings = null;
        try {
            strings = client.getChildren().forPath("/org.didinem.sample.service.TestService/providers");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String providerString = strings.get(0);
        String[] split = providerString.split(":");
        String hostAddress = split[0];
        int port = Integer.parseInt(split[1]);




    }

}
