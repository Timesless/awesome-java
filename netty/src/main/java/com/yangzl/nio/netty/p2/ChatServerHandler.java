package com.yangzl.nio.netty.p2;

import com.sun.org.apache.xpath.internal.operations.String;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: yangzl
 * @Date: 2020/1/1 17:08
 * @Desc: .. 实现上线提醒，离线提醒，消息转发
 **/
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

	/**
	 * 单例全局事件处理器
	 * 会自动遍历维护的所有channel
	 */
	public static ChannelGroup CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel channel = ctx.channel();
		System.out.println(msg);
		CHANNELS.forEach(ch -> {
			if (channel != ch) {
				StringBuilder sb = new StringBuilder(LocalDateTime.now().format(FORMATTER))
						.append("\r\n")
						.append(ch.remoteAddress())
						.append(": ")
						.append(msg);
				ch.writeAndFlush(sb.toString());
			} else {
				ch.writeAndFlush(msg);
			}
		});
	}
	
	/**
	 * @Date: 2020/1/1
	 * @Desc: 通道可用时被调用
	 **/
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.channel().remoteAddress() + "上线...");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.channel().remoteAddress() + "离线...");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		CHANNELS.add(channel);
		CHANNELS.writeAndFlush(channel.remoteAddress() + "加入群聊...");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		CHANNELS.writeAndFlush(ctx.channel().remoteAddress() + "离开群聊...");
	}
}
