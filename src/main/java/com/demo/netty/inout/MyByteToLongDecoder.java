package com.demo.netty.inout;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     *
     * decode方法会根据接收的数据，被调用多次，
     * 直到确定没有新的元素被加入到list中，或者ByteBuf没有更多的
     * 可读字节为止，如果list不为空，就会将list的内容传递给下一个处理器，
     * 该处理器的方法也会被调用多次
     * @param ctx 上下文对象
     * @param in 入站的ByteBuf
     * @param out list集合，将解码后的数据传给下一个handler
     * @throws Exception e
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder decode 被调用！");

        // 因为long为8个字节，需要判断有8个字节，才能读取一个long
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
