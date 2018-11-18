package org.didinem.transport.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.didinem.rpc.RpcRequest;
import org.didinem.rpc.RpcResponse;
import org.didinem.transport.ResponseFuture;

/**
 * Created by didinem on 5/7/2017.
 */
public class NettyClient {

    private Channel channel;

    public ResponseFuture<RpcResponse> request(RpcRequest rpcRequest) {
        ChannelFuture channelFuture = channel.writeAndFlush(rpcRequest);

        return null;
    }

}
