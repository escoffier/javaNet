package com.nettyexample;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class EchoOutHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //super.write(ctx, msg, promise);
        ByteBuf buffer = (ByteBuf) msg;
        System.out.println("EchooutHandler: " + buffer
                + "   ---current thread: " + Thread.currentThread().getName());
        ctx.write(msg);
        //ctx.fireChannelWritabilityChanged();
    }
}
