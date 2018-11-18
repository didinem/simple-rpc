package org.didinem.sample.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by didinem on 11/18/2018.
 */
public class SimpleTask implements Runnable {

    private ChannelHandlerContext ctx;

    public SimpleTask(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        int i = 10;
        while (i-- > 0) {
            ctx.writeAndFlush(Unpooled.copiedBuffer("another thread response", CharsetUtil.UTF_8));
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
