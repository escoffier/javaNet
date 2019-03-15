package com.nettyexample;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class TransServer {

    private static Logger logger = LoggerFactory.getLogger(TransServer.class);
    public static void main(String[] args) {

    }

    //private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(new NioEventLoopGroup(1), new NioEventLoopGroup(4))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<ServerChannel>() {
                    @Override
                    protected void initChannel(ServerChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ProtobufVarint32FrameDecoder());
                        pipeline.addLast(new ProtobufDecoder(HelloRequest.getDefaultInstance()));

                        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new TransHandler());

                    }
                });

        ChannelFuture future = serverBootstrap.bind();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

            }
        });

    }

    private static class TransHandler extends ChannelInboundHandlerAdapter {
        private static Logger logger = LoggerFactory.getLogger(TransHandler.class);

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            //super.channelActive(ctx);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            ch.pipeline().addLast(new ProtobufEncoder());
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            ch.pipeline().addLast(new ProtobufDecoder(HelloReply.getDefaultInstance()));
                            ch.pipeline().addLast(new TransHandler());
                        }
                    })
                    .group(ctx.channel().eventLoop());

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 19999));
            //channelFuture.addListener()
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //super.channelRead(ctx, msg);
            logger.info("transmit msg: " + msg.toString());
            ctx.writeAndFlush(msg);
        }
    }
}
