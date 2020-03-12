package com.demo.nio;

import java.nio.ByteBuffer;

public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        // 类型化方式放入数据
        byteBuffer.putInt(100);
        byteBuffer.putLong(9L);
        byteBuffer.putChar('a');
        byteBuffer.putShort((short) 4);

        // 取出数据
        byteBuffer.flip();
        System.out.println();
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getShort());
    }
}
