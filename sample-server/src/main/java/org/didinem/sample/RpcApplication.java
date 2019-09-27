package org.didinem.sample;

import org.didinem.sample.rpc.ProviderInfo;
import org.didinem.sample.rpc.ZKRegisterService;
import org.didinem.sample.server.rpc.ProviderRegisterService;
import org.didinem.sample.server.rpc.RpcServerService;
import org.didinem.sample.service.TestService;

import java.util.concurrent.TimeUnit;

/**
 * Created by didinem on 11/25/2018.
 */
public class RpcApplication {

    public static void main(String[] args) {
        testRegiste();
        testServer();

        try {
            TimeUnit.DAYS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testServer() {
        RpcServerService rpcServerService = new RpcServerService();

        ProviderInfo providerInfo = new ProviderInfo(TestService.class, "127.0.0.1", 20880);
        rpcServerService.initServer(providerInfo);
    }

    private static void testRegiste() {
        ZKRegisterService zkRegisterService = new ZKRegisterService();
        ProviderRegisterService providerRegisterService = new ProviderRegisterService();
        providerRegisterService.setZkRegisterService(zkRegisterService);

        ProviderInfo providerInfo = new ProviderInfo(TestService.class, "127.0.0.1", 20880);
        providerRegisterService.registe(providerInfo);
    }

}
