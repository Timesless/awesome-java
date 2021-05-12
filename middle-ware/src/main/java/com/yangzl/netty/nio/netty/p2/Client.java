package com.yangzl.netty.nio.netty.p2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
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
public class Client {

	public static void startServer() { startServer0(); }
	private static void startServer0() {
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
			ChannelFuture future = bootstrap.connect("127.0.0.1", 8888).sync();
			Channel channel = future.channel();
			/*future.addListener((ChannelFutureListener) cf -> {
				Channel channel = cf.channel();
				channel.writeAndFlush("hello server");
			});*/
			System.out.println("您可以发送消息了...");
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNextLine()) {
				channel.writeAndFlush(scanner.nextLine());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		startServer();
	}
}
