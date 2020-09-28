package com.yangzl.nio.netty.p1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author: yangzl
 * @Date: 2020/1/1 00:17
 * @Desc: ..
 */
public class NettyClient {
	
	public static void startClient() { startClient0(); }

	private static void startClient0() {
		NioEventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
				// 通道实现类
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						ChannelPipeline pipeline = channel.pipeline();
						pipeline.addLast(new ClientHandler());
					}
				});
		try {
			ChannelFuture future = bootstrap.connect("127.0.0.1", 6666).sync();
			System.out.println("已连接服务器...");
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) { startClient(); }
}
