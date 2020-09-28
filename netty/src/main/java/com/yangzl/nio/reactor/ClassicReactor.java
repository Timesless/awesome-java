package com.yangzl.nio.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: yangzl
 * @Date: 2020/3/14 13:53
 * @Desc: .. 经典Reactor模式（Scalable I/O in Java） -> 单线程
 * Reactor Pattern
 * 		<em>Reactor</em> responds to IO events by dispatching the appropriate handler
 * 		<em>Handlers</em> perform non-blocking actions 
 * 		Manage by binding handlers to events
 * 	1. Server Setup
 * 	2. Dispatch Loop (服务端分发连接事件 循环)
 * 	3. Acceptor  生成SocketChannel
 * 	4. Handler Setup
 * 	5. Request Handler
 * 	
 * 	使用putty raw连接可执行测试，注意这里putty将回车符也发送，所以总会打印回车
 * 	socket读写，不要使用-1来判断
 */
public class ClassicReactor implements Runnable {

	Selector selector;
	ServerSocketChannel serverChannel;
	
	public ClassicReactor() throws Exception {
		// 1 Selector.open() 调用 SPI
		// 2 SPI方式
		this.selector = SelectorProvider.provider().openSelector();
		// 创建并设置serverChannel
		serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.bind(new InetSocketAddress(6666));
		/*
		 * 自己管理SelectionKey
		 * 即调用attach绑定
		 */
		SelectionKey sk = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		sk.attach(new AcceptorHandler());
		System.out.println("服务器已启动...");
	}
	
	@Override
	public void run() {
		try {
			// 
			while (!Thread.interrupted()) {
				// block until events happen
				selector.select();
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				for (Iterator<SelectionKey> iter = selectionKeys.iterator(); iter.hasNext();) {
					SelectionKey event = iter.next();
					// 手动获取事件
					dispatch(event);
				}
				selectionKeys.clear();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	// Reactor 分发事件
	// 其实是分发 连接事件
	private void dispatch(SelectionKey event) {
		Runnable handler = (Runnable) event.attachment();
		// 单线程Reactor这里直接调用之前绑定对象的run
		if (null != handler) handler.run();
	}

	// inner class
	class AcceptorHandler implements Runnable {
		@Override
		public void run() {
			try {
				SocketChannel clientChannel = serverChannel.accept();
				if (clientChannel != null) {
					new IOHandler(selector, clientChannel);
					System.out.println("客户端已连接...");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new Thread(new ClassicReactor()).start();
	}
}

class IOHandler implements Runnable {
	SelectionKey sk;
	SocketChannel client;
	/**
	 * track 状态
	 * 定义状态常量并初始状态为READING
	 * 也可以为每个状态定义一个类（GoF State-Object Pattern）
	 * class Reader
	 * class Writer   sk.attach(new Writer()), sk.intersetOps(..OP_WRITER) sk.selector().wakeup()
	 */
	static final int READING = 0, WRITING = 1;
	int state = READING;
	// 读写缓冲区，定义为16是为了练习flip() & clear()
	ByteBuffer buffer = ByteBuffer.allocate(16);
	
	// 初始化
	public IOHandler(Selector selector, SocketChannel socketChannel) throws IOException {
		client = socketChannel;
		client.configureBlocking(false);
		// TODO 为什么这里注册0呢?，根本没有0事件
		sk = client.register(selector, 1);
		/*
		 * try fisrt read now
		 * 将自己绑定到当前SelectionKey
		 */
		sk.attach(this);
		sk.interestOps(SelectionKey.OP_READ);
		selector.wakeup();
	}

	@Override
	public void run() {
		try {
			if (state == READING) read();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void read() throws IOException {
		// 读完之后向客户端回显数据
		int len;
		// 将client的数据 写入到in buffer中
		while ((len = client.read(buffer)) > 0) {
			System.out.println("完成一次读取...");
			System.out.println(new String(buffer.array(), 0, len));
			/*
			 * 这里使用两个缓冲区好像不能完成功能
			 * 因为当我循环读完数据，in buffer中只剩下最后一次循环的数据
			 *
			 * 结论：所以每次缓冲区读完都需要切换为写，向客户端先回显这一部分数据
			 * 		但是并不是我所预期的结果，发送 > 16的字节，也被正确回显了
			 * 		被正确回显是因为在事件循环
			 * 所以上述结论是正确的
			 * 
			 * ****注意 channel的读 == buffer的写，切勿混淆****
			 */
			buffer.flip();
			sk.interestOps(SelectionKey.OP_WRITE);
			state = WRITING;
			echo();
		}
	}

	private void echo() throws IOException {
		client.write(buffer);
		System.out.println("完成一次回显...");
		buffer.clear();
		sk.interestOps(SelectionKey.OP_READ);
		state = READING;
	}
}

