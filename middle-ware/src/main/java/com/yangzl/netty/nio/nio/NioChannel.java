package com.yangzl.netty.nio.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author yangzl
 * @date 2019/12/28 22:25
 * @desc .. 可注册到selector上，对应一个buffer
 * 			FileChannle 阻塞
 * 			ServerSocketChannel、SocketChannel
 * 				ServerSocketChannelImpl, SocketChannelImpl	
 * 			DatagramChannel UDP协议
 * 	int read(ByteBuffer) 将通道数据放入buffer
 * 	int write(ByteBuffer) 将buffer数据写入通道
 * 	transferForm 将其它通道数据复制到当前通道
 * 	transferTo 将当前通道数据复制到其他通道
 * 	
 * 	硬盘/IO设备 -- buffer -- channel -- stream -- 应用程序
 * 	总结： 直接使用Files它不香吗
 */
public class NioChannel {
	
	/**
	 * @date 2019/12/28
	 * @desc  将字符串写入文件
	 */
	public static void str2File() {

		Path path = Paths.get("file.txt");
		try (FileChannel channel = FileChannel.open((Files.createFile(path)), StandardOpenOption.WRITE)){
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			buffer.put("this is seventeen".getBytes());
			// 切换为读
			buffer.flip();
			// Writes a sequence of bytes to this channel from the given buffer.
			channel.write(buffer);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @date 2019/12/28
	 * @desc 将文件数据读入显示到console
	 */
	public static void file2Str() {
		try (FileChannel channel = FileChannel.open(Paths.get("file.txt"), StandardOpenOption.READ)) {
			/*
			 * 知道这个文件长度有多少呢 2020年3月14日已解决
			 */
			ByteBuffer buffer = ByteBuffer.allocate(16);
			int len;
			while ((len = channel.read(buffer)) != -1) {
				System.out.println(new String(buffer.array(), 0, len));
				// 切换为写模式
				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @date 2019/12/28
	 * @desc 文件复制，将file.txt文件复制一份为file2.txt
	 */
	public static void copyFile() {
		try (FileChannel in = FileChannel.open(Paths.get("file.txt"), StandardOpenOption.READ);
			 FileChannel out = FileChannel.open(Files.createFile(Paths.get("file2.txt")), StandardOpenOption.WRITE)){ 
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (in.read(buffer) != -1) {
				// 切换为读
				buffer.flip();
				out.write(buffer);
				// 重置所有位置
				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @date 2019/12/29
	 * @desc transferTo 与 transferFrom 采用零拷贝（无须CPU拷贝，CPU配置DMA控制器去拷贝）实现
	 */
	public static void transfer() {
		Path inPath = Paths.get("file.txt");
		try (FileChannel in = FileChannel.open(inPath, StandardOpenOption.READ);
		FileChannel out = FileChannel.open(Files.createFile(Paths.get("file3.txt")), StandardOpenOption.WRITE)){
			// long len = Files.size(inPath);
			// in.transferTo(0, in.size(), out);
			out.transferFrom(in, 0, in.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 以下测试请按顺序执行
	public static void main(String[] args) {
		str2File();
		file2Str();
		copyFile();
		// 零复制实现
		transfer();
	}
}
