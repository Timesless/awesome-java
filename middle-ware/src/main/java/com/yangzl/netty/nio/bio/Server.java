package com.yangzl.netty.nio.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: yangzl
 * @Date: 2019/12/28 20:44
 * @Desc: .. 使用telnet 127.0.0.1 6666  ctrl+] send hello world 执行测试
 */
public class Server {

	public static void startServer() {
		try (ServerSocket serverSocket = new ServerSocket(6666);
			 Socket client = serverSocket.accept();
			 InputStream is = client.getInputStream()) {
			System.out.println("服务器已启动... ");
			while (true) {
				byte[] bytes = new byte[16];
				int len;
				while ((len = (is.read(bytes))) != -1) {
					String str = new String(bytes, 0, len);
					/*
					 * 乱码问题未解决，无法得知telnet用什么编码发送的
					 * 使用putty可解决乱码
					 */
					System.out.println(str);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		startServer();
	}
}
