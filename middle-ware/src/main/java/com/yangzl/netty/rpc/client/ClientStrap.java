package com.yangzl.netty.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author yangzl
 * @date 2019/12/28 12:46
 * @desc ..
 */
public class ClientStrap {
	
	public static void connectServer() {
		connectServer0();
	}

	private static void connectServer0() {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
					.channel(SocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							ChannelPipeline pipeline = socketChannel.pipeline();
							pipeline.addLast(new StringDecoder());
							pipeline.addLast(new StringEncoder());
							pipeline.addLast(new ClientHanlder());
						}
					});
			ChannelFuture future = bootstrap.connect("127.0.0.1", 6666).sync();
			future.channel().closeFuture();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

}
