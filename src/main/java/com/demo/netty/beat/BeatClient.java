package com.demo.netty.beat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

@SuppressWarnings("all")
public class BeatClient {

    private final String host;

    private final int port;

    public BeatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        // 客户端只需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建客户端启动对象
            Bootstrap bootstrap = new Bootstrap();
            // 设置相关参数
            bootstrap.group(group) // 设置线程组
                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类(反射)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 加入解码器，用于接收数据解码
                            pipeline.addLast("decoder", new StringDecoder());
                            // 加入编码器，用于发送数据编码
                            pipeline.addLast("encoder", new StringEncoder());
                            // 加入自定义业务处理handler
                            pipeline.addLast("myClientHandler",new BeatClientHandler());
                        }
                    });
            // 启动客户端去连接服务器端
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            // 得到channel
            Channel channel = channelFuture.channel();
            System.out.println("[客户端]地址：" + channel.localAddress());

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                // 发送到服务器端
                channel.writeAndFlush(msg + "\r\n");
            }
            // 给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        new BeatClient("127.0.0.1",  8888).run();
    }
}
