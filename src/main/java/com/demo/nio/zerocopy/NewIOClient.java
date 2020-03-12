package com.demo.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("localhost", 7001));

        String fileName = "bad.mp4";

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        // 准备发送数据
        long startTime = System.currentTimeMillis();

        // 在linux下一个transferTo方法就可以完成传输，windows下一次8M，需要分段
        long transferCount = fileChannel.transferTo(0, fileChannel.size(),
                socketChannel);
        System.out.println("发送的总字节数：" + transferCount + ",耗时：" +
                (System.currentTimeMillis() - startTime));

        fileChannel.close();
    }
}
