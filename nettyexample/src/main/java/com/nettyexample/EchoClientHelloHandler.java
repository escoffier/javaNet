package com.nettyexample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoClientHelloHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(EchoClientHelloHandler.class);

    private ChannelHandlerContext handlerContext;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
//        User user = new User();
//        user.setUserName("robbie");
//        user.setPassword("1234");


//        HelloRequest.Builder builder = HelloRequest.newBuilder();
//        builder.setName("robbie");
//        logger.info("send HelloRequest: ");
//        ctx.writeAndFlush(builder.build());
        handlerContext = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        logger.info("reply: "+msg.toString());
    }

    public void sayHello(String name) {
        HelloRequest.Builder builder = HelloRequest.newBuilder();

        builder.setName(name);

        logger.info("send HelloRequest: ");
        handlerContext.writeAndFlush(builder.build());
    }

}
