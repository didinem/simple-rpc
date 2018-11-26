package org.didinem.sample.server.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.didinem.sample.RpcInvocation;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Created by didinem on 5/20/2017.
 */
@ChannelHandler.Sharable
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

        threadPoolService.submitTask(new ServiceTask(ctx, rpcInvocation));
        ReferenceCountUtil.release(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
