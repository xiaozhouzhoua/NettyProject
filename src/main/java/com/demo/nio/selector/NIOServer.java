package com.demo.nio.selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception{
        // 使用ServerSocketChannel和SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到Socket并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();

        // 把ServerSocketChannel注册到Selector关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("注册后的的SelectorKey的数量：" + selector.keys().size());
        while (true) {
            // 阻塞一秒钟等待客户端连接
            if (selector.select(1000) == 0) {
                System.out.println("无连接");
                continue;
            }
            // 如果返回的大于零，就获取到相关的SelectorKeys监听事件集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("有事件发生的对应的channel的SelectorKey数量：" +
                    selectionKeys.size());

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 不同事件进行不同的处理
                if (key.isAcceptable()) {
                    // 有客户端连接时，生成客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成的SocketChannel的hash为：" +
                            socketChannel.hashCode());
                    // 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    // 将SocketChannel注册到Selector，关注事件为OP_READ，并关联一个buffer
                    socketChannel.register(
                            selector,
                            SelectionKey.OP_READ,
                            ByteBuffer.allocate(1024)
                    );
                    System.out.println("客户端连接后的SelectorKey的数量：" + selector.keys().size());
                }
                if (key.isReadable()) {
                    // 读事件通过key反向获取对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    channel.read(byteBuffer);
                    System.out.println("客户端发送的信息：" + new String(byteBuffer.array()));
                }
                // 手动从集合中移除selectKey，防止重复操作
                iterator.remove();
            }
        }
    }
}
