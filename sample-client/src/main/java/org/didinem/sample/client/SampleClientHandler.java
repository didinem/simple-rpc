package org.didinem.sample.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.didinem.sample.RpcInvocation;
import org.didinem.sample.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by didinem on 11/10/2018.
 */
public class SampleClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setInterfaceQualifiedName("org.didinem.sample.service.TestService");
        rpcInvocation.setMethodName("test");
        rpcInvocation.setParametersTypes(new Class[]{String.class});
        rpcInvocation.setParameters(new Object[]{"abc"});


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(rpcInvocation);
        byte[] objectByte = byteArrayOutputStream.toByteArray();
        objectOutputStream.close();
        byteArrayOutputStream.close();

        ctx.writeAndFlush(Unpooled.copiedBuffer(objectByte));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 接受请求消息
        ByteBuf byteBuf = msg;
        byte[] msgByte = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgByte);

        // 反序列化
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(msgByte);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        RpcResponse rpcResponse = (RpcResponse) object;
        System.out.println(rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
