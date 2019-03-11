package com.nettyexample;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;


public class UserEncoder extends MessageToByteEncoder<User> {
    private Logger logger = LoggerFactory.getLogger(UserEncoder.class);
    @Override
    protected void encode(ChannelHandlerContext ctx, User msg, ByteBuf out) throws Exception {
        /**
        * @Author: Robbie
        * @Description:
        * @param ctx
        * @param msg
        * @param out
        * @Return: void
        * @Date: 下午2:29 18-11-19 
        */
        ByteArrayOutputStream  byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteArrayOutputStream);

        output.writeObject(msg);
        output.flush();
        output.close();
        byte[] data = byteArrayOutputStream.toByteArray();

        logger.info("encode msg: " + msg.toString());
        out.writeBytes(data);


        //msg.toString();
    }
}

