package com.nettyexample;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class EchoServerPreHandler  extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(EchoServerPreHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
//        ByteBuf buffer = (ByteBuf) msg;
//        System.out.println("EchoServerPreHandler received msg: "+ buffer.toString(CharsetUtil.UTF_8)
//                + "   ---current thread: " + Thread.currentThread().getName());


//        System.out.println("EchoServerPreHandler received msg: "+ msg.toString()
//                + "   ---current thread: " + Thread.currentThread().getName());

        logger.info("received msg: " + msg.toString());
        ctx.fireChannelRead(msg);

    }
}
