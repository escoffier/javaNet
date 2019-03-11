package com.nettyexample;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDecoder extends ByteToMessageDecoder {
    private static Logger logger = LoggerFactory.getLogger(UserDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        byte[] data = new byte[in.readableBytes()];
                in.readBytes(data);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);

        Object obj = inputStream.readObject();
        //System.out.println(obj.toString());
        logger.info(obj.toString());

        out.add(obj);
    }
}
