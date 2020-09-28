package com.yangzl.nio.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @Author: yangzl
 * @Date: 2019/12/29 16:43
 * @Desc: ..
 */
public class ChatClient {

	private SocketChannel channel;
	private Selector selector;
	
	public ChatClient() { initClient(); }

	private void initClient() {
		try {
			selector = Selector.open();
			channel = SocketChannel.open();
			channel.connect(new InetSocketAddress("127.0.0.1", 6666));
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
			System.out.println("您可以发送消息了...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readMsg() {
		try {
			SelectionKey key;
			SocketChannel channel;
			ByteBuffer buffer;
			while (true) {
				if (selector.select(1000) <= 0) { continue; }
				for (Iterator<SelectionKey> iter = selector.selectedKeys().iterator(); iter.hasNext();) {
					key =  iter.next();
					if (key.isReadable()) {
						channel = (SocketChannel) key.channel();
						buffer = ByteBuffer.allocate(1024);
						channel.read(buffer);
						System.out.println(new String(buffer.array()));
					}
					iter.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(String msg) {
		ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
		try {
			channel.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		new Thread(() -> client.readMsg()).start();
		// console标准输入
		Scanner scanner = new Scanner(System.in);
		String msg;
		while (scanner.hasNextLine()) {
			msg = scanner.nextLine();
			client.sendMsg(msg);
		}
	}
}
