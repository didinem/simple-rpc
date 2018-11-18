package org.didinem.sample.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.didinem.sample.RpcInvocation;

import java.io.ByteArrayOutputStream;
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
        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
