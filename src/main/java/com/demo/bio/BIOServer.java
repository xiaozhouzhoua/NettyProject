package com.demo.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("all")
public class BIOServer {
    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");

        while (true) {
            // 阻塞在这里
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            threadPool.execute(() -> {
                handler(socket);
            });
        }
    }

    // 与客户端通信
    public static void handler(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            // 循环读取客户端发送的数据
            while (true) {
                // 一个客户端一个线程
                System.out.println("线程信息：" + Thread.currentThread().getName() +
                        " " + Thread.currentThread().getId());
                // 返回到底读取了多少数据，阻塞在这里
                int read = inputStream.read(bytes);
                // 还可以继续读
                if (read != -1) {
                    System.out.println(new String(bytes,
                            0 ,read));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和客户端的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
