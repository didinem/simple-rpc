package org.didinem.sample.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.didinem.sample.RpcInvocation;
import org.didinem.sample.RpcResponse;
import org.didinem.sample.service.TestServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by didinem on 11/18/2018.
 */
public class SimpleTask implements Runnable {

    private static Map<String, Object> map = new HashMap<>();

    static {
        map.put("org.didinem.sample.service.TestService", new TestServiceImpl());
    }

    private ChannelHandlerContext ctx;

    private RpcInvocation rpcInvocation;

    public SimpleTask(ChannelHandlerContext ctx, RpcInvocation rpcInvocation) {
        this.ctx = ctx;
        this.rpcInvocation = rpcInvocation;
    }

    @Override
    public void run() {
        // 执行方法调用
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


        // 发送返回消息
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setResponse(returnValue);

        byte[] objectByte = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream= new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(rpcResponse);
            objectByte = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx.writeAndFlush(Unpooled.copiedBuffer(objectByte));
    }

}
