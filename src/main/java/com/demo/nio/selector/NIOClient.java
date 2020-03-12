package com.demo.nio.selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws Exception{
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        // 设置非阻塞模式
        socketChannel.configureBlocking(false);

        // 提供服务器端的IP和Port
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",
                7000);

        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞");
            }
        }
        // 如果连接成功则发送数据
        String str = "hello";
        // 实用的wrap，无需指定大小和内容
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.trim().getBytes());
        // 发送数据，将buffer数据写入到channel
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
