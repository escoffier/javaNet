package com.nettyexample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoHelloHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(EchoHelloHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        logger.info(msg.toString());
    }
}
