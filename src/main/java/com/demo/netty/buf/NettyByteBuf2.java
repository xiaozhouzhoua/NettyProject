package com.demo.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyByteBuf2 {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello 你好!",
                CharsetUtil.UTF_8);

        // 使用相关方法
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();

            // 不读取空串
            System.out.println(new String(content, 0,
                    byteBuf.writerIndex(), CharsetUtil.UTF_8));

            System.out.println("byteBuf = " + byteBuf);
            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());

            // 返回第一个字节的ASCII码，不会改变readIndex
            System.out.println(byteBuf.getByte(0));

            // 返回字符
            System.out.println((char)byteBuf.getByte(0));

            // 可读的字节数，与readIndex有关
            int len = byteBuf.readableBytes();
            System.out.println(len);

            System.out.println(byteBuf.getCharSequence(0,
                    4, CharsetUtil.UTF_8));

        }
    }
}
