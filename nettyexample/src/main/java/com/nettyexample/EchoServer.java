package com.nettyexample;

import com.google.protobuf.MessageLite;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class EchoServer {

    private int port;
    private static Logger logger = LoggerFactory.getLogger(EchoServer.class);

    EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception{

        EchoServer echoServer = new EchoServer(19999);
        echoServer.start(ChannelType.EPOLL, CodecType.PROTOBUF);
    }

    public enum ChannelType {
        NIO, EPOLL
    }

    public enum CodecType {
        JBOSS, PROTOBUF
    }

    public void start(ChannelType channelType, CodecType codecType) throws Exception {
        final EchoServerHandler handler = new EchoServerHandler();

        EventLoopGroup group = null;

        switch (channelType)
        {
            case EPOLL:
                group = new EpollEventLoopGroup();
                break;
            case NIO:
                group = new NioEventLoopGroup();
                break;
                default:
                    group = new NioEventLoopGroup();
        }


        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group);
            switch (channelType)
            {
                case NIO:
                    bootstrap.channel(NioServerSocketChannel.class);
                    break;
                case EPOLL:
                    bootstrap.channel(EpollServerSocketChannel.class)
                    .localAddress(new InetSocketAddress("127.0.0.1",port))
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    logger.info("initChannel");
                                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                                    ch.pipeline().addLast(new ProtobufDecoder(HelloRequest.getDefaultInstance()));

                                    ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                                    ch.pipeline().addLast(new ProtobufEncoder());
                                    ch.pipeline().addLast(new EchoServerHelloHandler());

                                }
                            });;
                    break;
                default:
                    bootstrap.channel(NioServerSocketChannel.class);
            }

            switch (codecType)
            {
                case JBOSS:
                    bootstrap.localAddress(new InetSocketAddress("127.0.0.1",port))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    //ch.pipeline().addLast(new UserDecoder());
                                }
                            });
                    break;

                case PROTOBUF:
//                    bootstrap.localAddress(new InetSocketAddress("127.0.0.1",port))
//                            .childHandler(new ChannelInitializer<SocketChannel>() {
//                                @Override
//                                protected void initChannel(SocketChannel ch) throws Exception {
//                                    logger.info("initChannel");
//                                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
//                                    ch.pipeline().addLast(new ProtobufEncoder());
//                                    ch.pipeline().addLast(new ProtobufDecoder(HelloRequest.getDefaultInstance()));
//                                    ch.pipeline().addLast(new EchoServerHelloHandler());
//
//                                }
//                            });
                    break;
                default:
                    bootstrap.localAddress(new InetSocketAddress("127.0.0.1",port))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline().addLast(new UserDecoder());
//                            ch.pipeline().addLast(new EchoServerPreHandler());
//                            ch.pipeline().addLast(handler);
//                            ch.pipeline().addLast(new EchoOutHandler());
//                            ch.pipeline().addLast(new UserEncoder());
                                    ch.pipeline().addLast(new EchoServerHandler0());
                                }
                            });
                    break;
            }


            //bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            logger.info("Start server at port : " + port);
            //System.out.println("Start server at port : " + port);
            ChannelFuture future = bootstrap.bind().sync();

            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("----bind port " + port);
                    } else {
                        Throwable cause = future.cause();
                        cause.printStackTrace();
                    }

                }
            });
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();

        }
    }
}
