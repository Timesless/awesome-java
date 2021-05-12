package com.yangzl.jvm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author yangzl
 * @date 2020/12/22 23:28
 *
 * BTrace测试
 * 	BTrace可以动态修改程序，是因为它基于Instrument，它是JVMTI「Java Vitrual Machine Tool Interface」的重要组件
 * 		提供一套代理「Agent」机制，使第三方程序可以代理的方式访问和修改JVM内部的数据，如Alibaba的「Arthas」
 */

public class BTrace {
	
	/**
	 * BTrace在返回前查看a, b 及返回值
	 * @BTrace
	 * public class TracingScript {
	 *     @OnMethod(clazz="com.yangzl.jvm.BTrace", method="add", location=@Location(Kind.RETURN))
	 *     public static void func(@Self com.yangzl.jvm.BTrace instance,
	 *     			int a, int b, @Return int rs) {
	 *         println("调用堆栈");
	 *         jstack();
	 *         println(strcat("方法参数1：", str(a)));   
	 *         println(strcat("方法参数2：", str(b)));  
	 *         println(strcat("方法结果：", str(rs)));  
	 *     }
	 * }
	 * 
	 * @param a a
	 * @param  b b
	 * @return int
	 */
	public int add(int a, int b) {
		return a + b;
	}

	public static void main(String[] args) throws Exception {
		final BTrace trace = new BTrace();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for (int i = 0; i < 3; ++i) {
			br.readLine();
			int a = (int) Math.round(Math.random() * 1000);
			int b = (int) Math.round(Math.random() * 1000);
			System.out.println(trace.add(a, b));
		}
	}

	
}
