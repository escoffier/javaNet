package com.nettyexample;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.Constant;

import java.net.InetSocketAddress;

public class EchoClient {
    private final String host;
    private final int port;

    private EchoClientHelloHandler echoClientHelloHandler;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public enum CodecType {
        JBOSS, PROTOBUF
    }

    public void start(CodecType codecType) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();


        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port));
            switch (codecType)
            {
                case JBOSS:
                    break;
                case PROTOBUF:
                    bootstrap.handler(new LoggingHandler(LogLevel.INFO))
                            .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                            ch.pipeline().addLast(new ProtobufEncoder());

                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                            ch.pipeline().addLast(new ProtobufDecoder(HelloReply.getDefaultInstance()));

//                            ch.pipeline().addLast(new UserEncoder());
//                            ch.pipeline().addLast(new UserDecoder());
                            echoClientHelloHandler = new EchoClientHelloHandler();
                            ch.pipeline().addLast(echoClientHelloHandler);
                            ch.pipeline().addLast(new EchoHelloHandler());
//                            ch.pipeline().addLast(new EchoClientHandler0());
//                            //ch.pipeline().addLast(new EchoClientHandler1());

                        }
                    });
                    break;
                    default:
                        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {

                                ch.pipeline().addLast(new UserEncoder());
                                ch.pipeline().addLast(new UserDecoder());
                                ch.pipeline().addLast(new EchoClientHandler());
                                ch.pipeline().addLast(new EchoClientHandler0());
                                ch.pipeline().addLast(new EchoClientHandler1());

                            }
                        });

            }


            ChannelFuture future = bootstrap.connect().sync();
            echoClientHelloHandler.sayHello("escoffier");

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        EchoClient client = new EchoClient("127.0.0.1", 19999);

        for (int i = 0; i < 1; i++){
            client.start(CodecType.PROTOBUF);
        }

    }

}
