package com.yangzl.netty.nio.netty.p2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author yangzl
 * @date 2020/1/1 17:22
 */
public class NettyChatClient {

	private Channel channel;
	public void startServer() { startServer0(); }
	private void startServer0() {
		NioEventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap()
				.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast("decode", new StringDecoder());
						pipeline.addLast("encode", new StringEncoder());
						pipeline.addLast(new ChatClientHandler());
					}
				});
		try {
			ChannelFuture future = bootstrap.connect("127.0.0.1", 6666).sync();
			System.out.println("您可以发送消息了...");
			future.addListener((ChannelFutureListener) cf -> {
				channel = cf.channel();
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		NettyChatClient client = new NettyChatClient();
		Thread tClient = new Thread(client::startServer);
		tClient.start();
		tClient.join();
		System.out.println(client.channel);
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String msg = scanner.nextLine();
			client.channel.writeAndFlush(msg);
		}
	}
}
