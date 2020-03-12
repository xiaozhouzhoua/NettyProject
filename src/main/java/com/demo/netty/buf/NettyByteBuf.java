package com.demo.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * netty的ByteBuf不需要使用flip进行反转，
 * 因为底层维护了readIndex和writeIndex
 * 通过readIndex和writeIndex以及capacity，将ByteBuf
 * 分成3个区域：
 * 0->readIndex 已经读取的区域
 * readIndex->writeIndex 可读的区域
 * writeIndex->capacity 可写的区域
 */
public class NettyByteBuf {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer(10);

        // writeIndex会变化
        for (int i = 0; i < 10; i++) {
            buf.writeByte(i);
        }

        // 只是获取索引值，readIndex不会变化
        for (int i = 0; i < buf.capacity(); i++) {
            System.out.println(buf.getByte(i));
        }
        System.out.println("*****************");

        // readIndex会变化
        for (int i = 0; i < buf.capacity(); i++) {
            System.out.println(buf.readByte());
        }
    }
}
