package org.didinem.sample.client;

import org.didinem.sample.client.rpc.ConsumerRegisterService;
import org.didinem.sample.client.rpc.RpcClientService;
import org.didinem.sample.client.rpc.RpcInvocationHandler;
import org.didinem.sample.rpc.ZKRegisterService;
import org.didinem.sample.service.TestService;

import java.lang.reflect.Proxy;

public class ClientApplication {

    public static void main(String[] args) {
        ZKRegisterService zkRegisterService = new ZKRegisterService();
        ConsumerRegisterService consumerRegisterService = new ConsumerRegisterService();
        consumerRegisterService.setZkRegisterService(zkRegisterService);

        RpcInvocationHandler rpcInvocationHandler = new RpcInvocationHandler();
        rpcInvocationHandler.setConsumerRegisterService(consumerRegisterService);

        RpcClientService rpcClientService = new RpcClientService();
        rpcInvocationHandler.setRpcClientService(rpcClientService);

        TestService testService = (TestService) Proxy.newProxyInstance(ClientApplication.class.getClassLoader(), new Class[]{TestService.class}, rpcInvocationHandler);

        String result = testService.test("abc");
        System.out.println(result);
    }

}
