package com.yangzl.netty.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author yangzl
 * @date 2019/12/28 13:27
 * @desc ..
 */
public class ClientHanlder extends ChannelInboundHandlerAdapter {

	
	/**
	 * @date 2019/12/28
	 * @desc 与服务器创建连接之后被触发
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ClientHanlder.channelActive...");
	}

	/**
	 * @date 2019/12/28
	 * @desc 收到服务器消息被触发
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("ClientHanlder.channelRead...");
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
