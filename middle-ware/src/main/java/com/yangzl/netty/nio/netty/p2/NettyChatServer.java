package com.yangzl.netty.nio.netty.p2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author yangzl
 * @date 2020/1/1 16:58
 */
public class NettyChatServer {

	public static void startServer() { startServer0(); }
	private static void startServer0() {
		NioEventLoopGroup boss = new NioEventLoopGroup(1);
		NioEventLoopGroup worker = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap()
				.group(boss, worker)
				.channel(NioServerSocketChannel.class)
				.option(NioChannelOption.SO_BACKLOG, 128)
				.childOption(NioChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						ChannelPipeline pipeline = channel.pipeline();
						pipeline.addLast("decode", new StringDecoder());
						pipeline.addLast("encode", new StringEncoder());
						pipeline.addLast("chatServerHandler", new ChatServerHandler());
					}
				});
		try {
			ChannelFuture future = bootstrap.bind(8888).sync();
			System.out.println("服务器已启动...");
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	public static void main(String[] args) { startServer(); }
}
