package com.demo.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class ChatClient {

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 6667;

    private Selector selector;

    private SocketChannel socketChannel;

    private String username;

    public ChatClient() throws IOException {
        selector = Selector.open();
        // 连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        // 设置为非阻塞
        socketChannel.configureBlocking(false);
        // 注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress()
                .toString().substring(1);
        System.out.println("客户端准备好了：" + username);
    }

    // 向服务器发送消息
    public void sendInfo(String info) {
        info = username + "说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取服务端发送的消息
    public void readInfo() {
        try {
            int countChannel = selector.select();
            // 有可用的通道
            if (countChannel > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                    iterator.remove();
                }
            }else{
                //System.out.println("没有可用的通道...");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // 启动客户端
        ChatClient chatClient = new ChatClient();
        // 启动线程读取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    chatClient.readInfo();
                    try{
                        TimeUnit.SECONDS.sleep(3);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // 发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }

    }
}
