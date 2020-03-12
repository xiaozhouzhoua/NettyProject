package com.demo.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering：将数据写入到buffer时，可以采用buffer数组，依次写入
 * Gathering：从buffer读取数据时，可以采用buffer数组，依次读
 */
public class ScatteringAndGathering {
    public static void main(String[] args) throws Exception{
        // 使用ServerSocketChannel和SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到Socket并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建一个buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];

        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端连接-telnet
        SocketChannel socketChannel = serverSocketChannel.accept();
        //假定从客户端接收8个字节
        int messageLength = 8;
        // 循环读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long readLength = socketChannel.read(byteBuffers);
                byteRead += readLength;
                System.out.println("读取的数据长度：" + byteRead);
                // 使用流打印，查看当前这个buffer的position和limit
                Arrays.stream(byteBuffers)
                        .map(b -> "position:" + b.position() + ", limit:" +b.limit())
                        .forEach(System.out::println);
            }
            // 将所有的buffer进行反转
            Arrays.asList(byteBuffers).forEach(ByteBuffer::flip);
            // 将数据读出显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long writeLength = socketChannel.write(byteBuffers);
                byteWrite += writeLength;
            }
            // 将所有的buffer进行clear
            Arrays.asList(byteBuffers).forEach(ByteBuffer::clear);

            System.out.printf("一共读取%d个字节, 一共写入%d个字节", byteRead, byteWrite);
            System.out.println();
        }
    }
}
