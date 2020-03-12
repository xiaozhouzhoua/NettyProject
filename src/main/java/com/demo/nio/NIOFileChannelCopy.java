package com.demo.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannelCopy {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("netty.png");
        FileOutputStream fileOutputStream = new FileOutputStream("netty-copy.png");

        // 获取各个流对应的fileChannel
        FileChannel from = fileInputStream.getChannel();
        FileChannel to = fileOutputStream.getChannel();

        // 使用transferFrom完成拷贝
        to.transferFrom(from, 0, from.size());

        // 关闭流和通道
        from.close();
        to.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
