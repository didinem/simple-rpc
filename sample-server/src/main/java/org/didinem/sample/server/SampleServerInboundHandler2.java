/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.didinem.sample.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.didinem.sample.RpcInvocation;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class SampleServerInboundHandler2 extends ChannelInboundHandlerAdapter {

    private ThreadPoolService threadPoolService;

    public SampleServerInboundHandler2(ThreadPoolService threadPoolService) {
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

        threadPoolService.submitTask(new SimpleTask(ctx, rpcInvocation));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
