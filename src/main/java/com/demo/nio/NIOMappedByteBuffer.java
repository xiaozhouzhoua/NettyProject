package com.demo.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer可让文件直接在内存修改
 * 操作系统不需要拷贝一次，是操作系统级别的修改，
 * 效率较高
 */
public class NIOMappedByteBuffer {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt",
                "rw");

        // 获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数一：FileChannel.MapMode.READ_WRITE读写模式
         * 参数二：0，表示可以直接修改的起始位置
         * 参数三：5，表示映射到内存的大小，即将1.txt的多少字节映射到内存，
         * 可以直接修改的范围就是0-5个字节
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE,
                0, 5);

        mappedByteBuffer.put(0, (byte) 'G');
        mappedByteBuffer.put(4, (byte) 'G');
        randomAccessFile.close();
    }
}
