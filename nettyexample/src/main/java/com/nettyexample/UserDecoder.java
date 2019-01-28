package com.nettyexample;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class UserDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        byte[] data = new byte[in.readableBytes()];
                in.readBytes(data);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);

        Object obj = inputStream.readObject();
        System.out.println(obj.toString());

        out.add(obj);
    }
}
