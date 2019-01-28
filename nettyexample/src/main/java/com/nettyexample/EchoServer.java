package com.nettyexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    private int port;

    EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception{

        EchoServer echoServer = new EchoServer(19999);
        echoServer.start();
    }

    public void start() throws Exception {
        final EchoServerHandler handler = new EchoServerHandler();

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress("127.0.0.1",port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new UserDecoder());
                            ch.pipeline().addLast(new UserEncoder());
                            ch.pipeline().addLast(new EchoServerPreHandler());
                            ch.pipeline().addLast(handler);

                            //ch.pipeline().addLast(new EchoOutHandler());



                        }
                    });

            System.out.println("Start server at port : " + port);
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
