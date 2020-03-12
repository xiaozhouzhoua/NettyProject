package com.demo.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannelWrite {
    public static void main(String[] args) throws Exception {
        String str = "hello channel";
        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("1.txt");

        // 通过输出获取对应的channel
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将数据放入到缓冲区
        byteBuffer.put(str.getBytes());

        // 转换为读模式
        byteBuffer.flip();

        // 将byteBuffer写入到channel中
        fileChannel.write(byteBuffer);

        // 关闭流
        fileOutputStream.close();
    }
}
