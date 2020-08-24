package com.yangzl.nio.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author: yangzl
 * @Date: 2020/1/1 13:48
 * @Desc: .. codec：code & decode编解码器
 **/
public class HttpServerHandlerInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast("httpServerCodec", new HttpServerCodec());
		pipeline.addLast("httpServerHandler", new HttpServerHandler());
	}
}
