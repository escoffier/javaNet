package com.nettyexample;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class EchoServerPreHandler  extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
//        ByteBuf buffer = (ByteBuf) msg;
//        System.out.println("EchoServerPreHandler received msg: "+ buffer.toString(CharsetUtil.UTF_8)
//                + "   ---current thread: " + Thread.currentThread().getName());


        System.out.println("EchoServerPreHandler received msg: "+ msg.toString()
                + "   ---current thread: " + Thread.currentThread().getName());
        ctx.fireChannelRead(msg);

    }
}
