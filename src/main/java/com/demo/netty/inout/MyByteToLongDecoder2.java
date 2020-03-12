package com.demo.netty.inout;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * ReplayingDecoder解码器扩展了ByteToMessageDecoder，
 * 可以不调用readableBytes方法，需指定用户状态管理的类型，Void表示
 * 不需要状态管理
 * 缺点：并不是所有的ByteBuf操作都支持，某些情况下，如网络速度慢且消
 * 息格式复杂的情况下，消息会被切割成多个碎片，
 * 速度会不如ByteToMessageDecoder
 */
@SuppressWarnings("all")
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder2 decode 被调用！");
        out.add(in.readLong());
    }
}
