package org.didinem.rpc.server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.didinem.rpc.RpcRequest;

/**
 * Created by didinem on 4/9/2017.
 */
public class RpcServerChannelHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest) msg;


        super.channelRead(ctx, msg);
    }
}
