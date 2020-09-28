package com.yangzl.nio.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author: yangzl
 * @Date: 2020/1/1 13:45
 * @Desc: ..
 */
public class HttpServer {

	public static void startServer() { startServer0(); }
	private static void startServer0() {
		NioEventLoopGroup boss = new NioEventLoopGroup(1);
		NioEventLoopGroup worker = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boss, worker)
				// 通道采用NioServerSocketChannel实现，反射获取
				.channel(NioServerSocketChannel.class)
				.childHandler(new HttpServerHandlerInitializer());
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
