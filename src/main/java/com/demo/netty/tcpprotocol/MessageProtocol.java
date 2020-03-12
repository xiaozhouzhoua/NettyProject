package com.demo.netty.tcpprotocol;

/**
 * 协议包
 */
@SuppressWarnings("all")
public class MessageProtocol {
    // 内容长度
    private int len;

    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
