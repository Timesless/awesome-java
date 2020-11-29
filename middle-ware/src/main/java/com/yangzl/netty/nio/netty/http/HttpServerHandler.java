package com.yangzl.netty.nio.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @Author: yangzl
 * @Date: 2020/1/1 13:51
 * @Desc: .. http无状态协议，每次通讯完成断开连接
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;
			// 不处理图标请求
			if (request.uri().contains("favicon")) {
				return;
			}
			System.out.println(ctx.channel().remoteAddress());
			System.out.println("handler hash = " + this.hashCode());
			ByteBuf buf = Unpooled.copiedBuffer("hello, httpClient", CharsetUtil.UTF_8);
			// 构造http响应协议头
			FullHttpResponse response =
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
			response.headers()
					.set(HttpHeaderNames.CONTENT_TYPE, "text/plain")
					.set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
			ctx.writeAndFlush(response);
		}
	}
}
