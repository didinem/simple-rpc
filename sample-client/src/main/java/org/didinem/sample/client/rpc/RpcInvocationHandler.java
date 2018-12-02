package org.didinem.sample.client.rpc;

import lombok.Setter;
import org.didinem.sample.rpc.ProviderInfo;
import org.didinem.sample.rpc.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by didinem on 11/29/2018.
 */
@Setter
public class RpcInvocationHandler implements InvocationHandler {

    private ConsumerRegisterService consumerRegisterService;

    private RpcClientService rpcClientService;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?>[] interfaces = proxy.getClass().getInterfaces();
        List<ProviderInfo> providerInfos = consumerRegisterService.subscribeAndList(interfaces[0]);
        if (providerInfos == null || providerInfos.size() == 0) {
            return null;
        }

        ProviderInfo providerInfo = providerInfos.get(0);
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setInterfaceQualifiedName(providerInfo.getQualifiedClassName());
        rpcInvocation.setMethodName(method.getName());

        Class[] parametersTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parametersTypes[i] = args[i].getClass();
        }
        rpcInvocation.setParametersTypes(parametersTypes);
        rpcInvocation.setParameters(args);

        rpcClientService.initClient(providerInfo.getHostAddress(), providerInfo.getPort());
        ResponseFuture responseFuture = rpcClientService.sendRequest(rpcInvocation);
        Object o = responseFuture.get();

        return o;
    }

}
