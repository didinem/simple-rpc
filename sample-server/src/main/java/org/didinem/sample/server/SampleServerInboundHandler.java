package org.didinem.sample.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.didinem.sample.RpcInvocation;
import org.didinem.sample.RpcResponse;

import java.io.*;

/**
 * Created by didinem on 5/20/2017.
 */
public class SampleServerInboundHandler extends ChannelInboundHandlerAdapter {

    private ThreadPoolService threadPoolService;

    public SampleServerInboundHandler(ThreadPoolService threadPoolService) {
        this.threadPoolService = threadPoolService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接受请求消息
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] msgByte = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgByte);

        // 反序列化
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(msgByte);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        RpcInvocation rpcInvocation = (RpcInvocation) object;
        System.out.println(rpcInvocation);

        final ChannelHandlerContext ctxRef = ctx;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                // 发送返回消息
                RpcResponse rpcResponse = new RpcResponse();
                rpcResponse.setResponse("aaa");

                byte[] objectByte = null;
                try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                     ObjectOutputStream objectOutputStream= new ObjectOutputStream(byteArrayOutputStream)) {
                    objectOutputStream.writeObject(rpcResponse);
                    objectByte = byteArrayOutputStream.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ctxRef.writeAndFlush(objectByte);
            }
        };
        Thread thread = new Thread(task);
        thread.start();

//        threadPoolService.submitTask(new ServiceTask(ctx, rpcInvocation));
        ReferenceCountUtil.release(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
