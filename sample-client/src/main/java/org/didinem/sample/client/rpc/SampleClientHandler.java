package org.didinem.sample.client.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.didinem.sample.rpc.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Created by didinem on 11/10/2018.
 */
public class SampleClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

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

        ResponseFuture responseFuture = ResponseFutureMap.get(rpcResponse.getRpcID());
        responseFuture.receive(rpcResponse.getResponse());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
