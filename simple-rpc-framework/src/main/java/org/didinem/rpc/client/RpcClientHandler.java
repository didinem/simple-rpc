package org.didinem.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.didinem.rpc.RpcResponse;
import org.didinem.transport.ResponseFuture;

/**
 * Created by didinem on 5/7/2017.
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        ResponseFuture<RpcResponse> responseFuture = RpcClient.responseFutureMap.get(msg.getRpcId());
        responseFuture.set(msg);
    }
}
