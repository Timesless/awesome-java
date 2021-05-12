package com.yangzl.juc;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author yangzl
 * @date 2020/1/10 15:50
 */
public class MultiThread {

	public static void main(String[] args) {

		// 静态工厂方法
		ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
		ThreadInfo[] threads = mxBean.dumpAllThreads(false, false);
		for (ThreadInfo thread : threads) {
			System.out.println("[" + thread.getThreadId() + "] " + thread.getThreadName());
		}
	}
}
