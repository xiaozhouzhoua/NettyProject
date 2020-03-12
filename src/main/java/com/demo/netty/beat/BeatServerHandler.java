package com.demo.netty.beat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

@SuppressWarnings("all")
public class BeatServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * evt表示事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress()
                    + "，超时事件：" + eventType);
            System.out.println("服务器开始处理....");

            // 选择是否关闭channel，即一旦发生空闲将通道立即关闭
            // ctx.channel().close();
        }
    }
}
