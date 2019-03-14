package com.nettyexample;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoClientHandler1 extends ChannelInboundHandlerAdapter {
    static Logger logger = LoggerFactory.getLogger(EchoClientHandler1.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        User user = (User) msg;
        logger.info(user.toString());
        logger.info("release msgs");
        ReferenceCountUtil.release(msg);
    }
}
