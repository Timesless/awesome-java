package com.yangzl.netty.nio.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @Author: yangzl
 * @Date: 2019/12/29 16:43
 * @Desc: .. 注意处理客户端关闭连接的情况
 */
public class ChatServer {

	private Selector selector;
	private ServerSocketChannel serverChannel;
	
	public ChatServer() { initServer(); }

	private void initServer() {
		try {
			selector = Selector.open();
			serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			serverChannel.bind(new InetSocketAddress(6666));
			System.out.println("服务器已启动...");
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listen() {
		SocketChannel channel = null;
		try {
			SelectionKey key;
			while (true) {
				
				/*
				 * 阻塞一秒，没有连接则进行下一次处理
				 * 阻塞的这一秒内其实可以做其它工作「如果有的话」
				 */
				if (selector.select(1000) <= 0) { continue; }
				for (Iterator<SelectionKey> iter = selector.selectedKeys().iterator(); iter.hasNext();) {
					 key = iter.next();
					if (key.isAcceptable()) {
						channel = serverChannel.accept();
						System.out.println(channel.getRemoteAddress() + "上线...");
						channel.configureBlocking(false);
						channel.register(selector, SelectionKey.OP_READ);
					}
					if (key.isReadable()) {
						channel = (SocketChannel) key.channel();
						forwardMsg(channel);
					 }
					 iter.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * @Date: 2019/12/29
	 * @Desc: 转发消息。 读取发生异常，可能是客户端关闭连接
	 */
	private void forwardMsg(SocketChannel channel) {
		ByteBuffer inBuffer = ByteBuffer.allocate(1024);
		SelectionKey key = null;
		try {
			channel.read(inBuffer);
			String msg = new String(inBuffer.array());
			System.out.println(channel.getRemoteAddress() + ": " + msg);
			ByteBuffer outBuffer;
			Channel ch;
			SocketChannel socketChannel;
			for (Iterator<SelectionKey> iter = selector.keys().iterator(); iter.hasNext();) {
				key = iter.next();
				ch = key.channel();
				if (ch instanceof SocketChannel && ch != channel) {
					// 确定类型之后再强转
					socketChannel = (SocketChannel) ch;
					outBuffer = ByteBuffer.wrap((socketChannel.getRemoteAddress() + ": " + msg).getBytes());
					socketChannel.write(outBuffer);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				System.out.println(channel.getRemoteAddress() + "下线...");
				if (null != key) {
					key.cancel();
				}
				channel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new ChatServer().listen();
	}
}
