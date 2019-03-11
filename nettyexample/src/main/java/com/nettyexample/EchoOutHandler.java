package com.nettyexample;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoOutHandler extends ChannelOutboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(EchoOutHandler.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //super.write(ctx, msg, promise);
        ByteBuf buffer = (ByteBuf) msg;
//        System.out.println("EchooutHandler: " + buffer
//                + "   ---current thread: " + Thread.currentThread().getName());
        logger.info(((ByteBuf) msg).toString());
        ctx.write(msg);
        //ctx.fireChannelWritabilityChanged();
    }
}
