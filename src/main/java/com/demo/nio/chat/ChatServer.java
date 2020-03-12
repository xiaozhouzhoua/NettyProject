package com.demo.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("all")
public class ChatServer {

    private Selector selector;

    private ServerSocketChannel listenChannel;

    private static final int PORT = 6667;

    public ChatServer() {
        try{
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            // 将channel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听
    public void listen() {
        try {
            while (true) {
                int countChannel = selector.select();
                if (countChannel > 0) {
                    // 遍历得到的selectionKey集合
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            // 提示客户上线
                            System.out.println(sc.getRemoteAddress() + "上线了");
                        }
                        if (key.isReadable()) {
                            // 处理读
                            readData(key);
                        }
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待。。。");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    // 读取客户端消息
    private void readData(SelectionKey key) {
        // 定义一个SocketChannel
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            // 创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 从channel读取数据
            int readCount = channel.read(buffer);
            if (readCount > 0) {
                // 从客户端读取到数据，转换为字符串
                String msg = new String(buffer.array());
                System.out.println("from 客户端：" + msg);
                // 服务端向其他客户端(去除自己)转发消息
                sendInfoToOtherClients(msg, channel);
            }
        }catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了...");
                // 取消注册
                key.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // 转发消息到其他客户
    private void sendInfoToOtherClients(String msg, SocketChannel self)
            throws IOException {
        System.out.println("服务器转发消息中...");
        for(SelectionKey key: selector.keys()) {
            // 通过key取出对应的SocketChannel
            Channel targetChannel = key.channel();
            // 排除自己
            if (targetChannel instanceof SocketChannel
                    && targetChannel != self) {
                SocketChannel des = (SocketChannel) targetChannel;
                // 将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                des.write(buffer);
            }
        }
    }
    public static void main(String[] args) {
        // 创建一个服务器对象
        ChatServer chatServer = new ChatServer();

        chatServer.listen();
    }
}
