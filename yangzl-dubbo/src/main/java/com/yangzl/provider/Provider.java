package com.yangzl.provider;

import com.yangzl.protocol.http.HttpServer;
import com.yangzl.provider.api.HelloService;
import com.yangzl.provider.impl.HelloServiceImpl;

/**
 * @author yangzl
 * @date 2021/3/29
 *
 */
public class Provider {

	public static void main(String[] args) throws Exception {

		// 本地注册
		LocalRegister.regist(HelloService.class.getName(), HelloServiceImpl.class);

		// 远程注册


		// 启动服务
		HttpServer server = new HttpServer();
		server.start("localhost", 8080);
		System.in.read();
	}

}
