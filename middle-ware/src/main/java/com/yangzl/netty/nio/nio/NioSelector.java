package com.yangzl.netty.nio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author yangzl
 * @date 2019/12/28 22:25
 * @desc .. IO multiplexing，通道以事件的方式注册到selector
 * 		Selector.open()	
 * 		int select(long timeout)：阻塞|超时阻塞 查询（select|epoll）
 * 			将有IO的SelectionKey加入Set<SelectionKey>	
 * 		Set<SelectionKey> selectedKeys(): 具体发生IO事件的所有SelectionKey集合
 */
public class NioSelector {
	
	private static int count = 0;
	/**
	 * @date 2019/12/29
	 * @desc 不同事件的处理可作为一个handler
	 */
	public static void startServer() {
		try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
			Selector selector = Selector.open();
			// 绑定端口，进行监听
			serverChannel.bind(new InetSocketAddress(6666));
			System.out.println("服务器已启动...");
			// 设置为非阻塞
			serverChannel.configureBlocking(false);
			// 将severChannel的连接事件注册到selector
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			// 自旋，处理
			SelectionKey key;
			SocketChannel channel;
			while (true) {
				// 阻塞查询1s，若没有事件那么可执行其它逻辑，这里不处理其它逻辑
				if (selector.select(1000) <= 0) { continue; }
				// 有事件，那么获取所有发生IO的SelectionKey
				Set<SelectionKey> keys = selector.selectedKeys();
				for (Iterator<SelectionKey> iter = keys.iterator(); iter.hasNext();) {
					key = iter.next();
					// 连接事件，基于事件驱动，这里不会阻塞
					if (key.isAcceptable()) {
						channel = serverChannel.accept();
						channel.configureBlocking(false);
						System.out.println("一个客户端连接成功...");
						// 注册到selector
						channel.register(selector, SelectionKey.OP_READ);
					}
					if (key.isReadable()) {
						SocketChannel clientChannel = (SocketChannel) key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						clientChannel.read(buffer);
						System.out.println(new String(buffer.array()));
						System.out.print(++count);
					}
					// 移除已经处理过IO的SelectionKey
					iter.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		startServer();
	}
}
