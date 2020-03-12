package com.demo.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(5);
        // 向buffer中存放数据
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put(i * 2);
        }
        // 从buffer中读取数据，先切换为读模式，再读
        buffer.flip();
        buffer.position(1);
        buffer.limit(3);
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
