package com.nettyexample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServerHandler0 extends SimpleChannelInboundHandler<User> {
    private static Logger logger = LoggerFactory.getLogger(EchoServerHandler0.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, User msg) throws Exception {
        logger.info(msg.toString());
        msg.setUserName("server-robbie");
        ctx.pipeline().write(msg);
    }
}
