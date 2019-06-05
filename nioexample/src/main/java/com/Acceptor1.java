package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

public class Acceptor1 implements Runnable {

    String name;
    private static final Logger logger = LoggerFactory.getLogger(Acceptor1.class);
    Selector acceptSelector;

    ServerSocketChannel serverSocketChannel;
    DispatcherPool dispatcherPool;

    public Acceptor1(ServerSocketChannel serverSocketChannel, DispatcherPool dispatcherPool) throws Exception {
        this.dispatcherPool = dispatcherPool;
        this.serverSocketChannel = serverSocketChannel;
        //this.acceptSelector = SelectorProvider.provider().openSelector();
        //serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        System.out.println("before accept new connection");
        for (; ; ) {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                logger.debug("accept new connection {}", socketChannel.getRemoteAddress());
                socketChannel.configureBlocking(false);

                ChannelIO channelIO = ChannelIO.getInstance(socketChannel, false);
                RequestHandler requestHandler = new RequestHandler(channelIO);
                dispatcherPool.register(socketChannel, SelectionKey.OP_READ, requestHandler);
            } catch (IOException ex) {
                System.out.println("accept ex: " + ex.getMessage());
            }

        }

    }
}
