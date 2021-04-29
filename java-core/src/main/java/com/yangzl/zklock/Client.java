package com.yangzl.zklock;

import java.util.stream.IntStream;

/**
 * @author yangzl
 * @date 2020/5/31 23:08
 *  测试代码
 */
public class Client {

	static class OrderUtil {
		private static int count;
		private final ZkDistributedLock lock = new ZkDistributedLock();
		public void getOrder() {
			try {
				lock.zkLock();
				System.out.printf("------------: %d\n", ++count);
			} finally {
				lock.zkUnLock();
			}
		}
	}
	
	public static void main(String[] args) {
		// 每次new OrderUtil 可模拟多个JVM，Spring每个Service为单例
		IntStream.rangeClosed(0, 50)
				.forEach(n -> new Thread(() -> new OrderUtil().getOrder(), "T" + n).start());
	}

}


