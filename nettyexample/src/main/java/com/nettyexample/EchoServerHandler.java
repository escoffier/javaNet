package com.nettyexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(EchoServerHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
//        ByteBuf buffer = (ByteBuf) msg;
//        System.out.println("received msg: "+ buffer.toString(CharsetUtil.UTF_8)
//                + "   ---current thread: " + Thread.currentThread().getName());
//        ctx.pipeline().write(buffer);

//                System.out.println("received msg: "+ msg.toString()
//                + "   ---current thread: " + Thread.currentThread().getName());

        logger.info("received msg: " + msg.toString());
                ctx.pipeline().write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //super.channelReadComplete(ctx);
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
