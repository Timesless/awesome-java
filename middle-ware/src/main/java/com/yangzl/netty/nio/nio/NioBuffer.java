package com.yangzl.netty.nio.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author yangzl
 * @date 2019/12/28 22:19
 *
 * 	NIO Buffer核心概念
 * 		mark, position, limit, capacity
 * 		flip 切换为读模式
 * 		reset position = mark	
 * 		rewind 重读
 * 		clear 所有位置重置为初始状态	
 * 	
 * 	数据在buffer中达到一定数量再执行读取，一个channel对应一个buffer，数据读入写出都需要通过buffer
 */
public class NioBuffer {

	/**
	 * @date 2019/12/29
	 * 堆外内存
	 */
	public static void mappedByteBuffer() {
		try {
			RandomAccessFile accessFile = new RandomAccessFile("file.txt", "rw");
			FileChannel channel = accessFile.getChannel();
			MappedByteBuffer directBuffer = channel
					.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
			directBuffer.put(2, (byte) 'C');
			accessFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @date 2019/12/29
	 *  分散：将channel数据依次写入到buffer数组
	 * 	聚合： 将buffer数组的数据依次写入到channel
	 */
	public static void scatterGather() {
		
	}
	
}
