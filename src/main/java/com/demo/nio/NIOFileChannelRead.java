package com.demo.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannelRead {
    public static void main(String[] args) throws Exception {
        File file = new File("1.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        // 创建Channel
        FileChannel fileChannel = fileInputStream.getChannel();

        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // 将通道的数据读入到buffer
        fileChannel.read(byteBuffer);

        // 将buffer中的字节数据转换为string
        System.out.println(new String(byteBuffer.array()));

        fileInputStream.close();
    }
}
