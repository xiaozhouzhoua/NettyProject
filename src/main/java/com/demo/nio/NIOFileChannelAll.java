package com.demo.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannelAll {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        // 循环读取
        while (true) {
            // 复位position=0，limit=capacity
            byteBuffer.clear();
            // 如果不复位limit=position，read返回0，read将永远不为-1
            int read = fileInputStreamChannel.read(byteBuffer);
            System.out.println("读取的内容大小：" + read);
            if (read == -1) {
                // 读完退出
                break;
            }
            // 转换为写模式
            byteBuffer.flip();
            // write后position到最后即limit=position
            fileOutputStreamChannel.write(byteBuffer);
        }
        // 关闭流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
