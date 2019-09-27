package org.didinem.sample.server.test;

import org.didinem.sample.rpc.RpcInvocation;
import org.didinem.sample.service.TestService;
import org.didinem.sample.service.TestServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by didinem on 11/10/2018.
 */
public class InvokerTest {

    private static Map<String, Object> map = new HashMap<>();

    static {
        map.put("org.didinem.sample.service.TestService", new TestServiceImpl());
    }

    public static void main(String[] args) {
        Class<TestService> clazz = TestService.class;

        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setInterfaceQualifiedName("org.didinem.sample.service.TestService");
        rpcInvocation.setMethodName("test");
        rpcInvocation.setParametersTypes(new Class[]{String.class});
        rpcInvocation.setParameters(new Object[]{"abc"});

        String invoke = invoke(rpcInvocation);
        System.out.println(invoke);
    }

    private static String invoke(RpcInvocation rpcInvocation) {
        Object service = map.get(rpcInvocation.getInterfaceQualifiedName());
        Class clazz = service.getClass();

        Method serviceMethod = null;
        try {
            serviceMethod = clazz.getMethod(rpcInvocation.getMethodName(), rpcInvocation.getParametersTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Object returnValue = null;
        try {
            returnValue = serviceMethod.invoke(service, rpcInvocation.getParameters());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (String) returnValue;
    }

}
