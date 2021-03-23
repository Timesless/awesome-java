package com.yangzl.jvm;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author yangzl
 * @date 2021/3/14
 * @desc	Q：方法中定义的局部变量是否线程安全？
 * 		A：取决于 局部变量 是否发生逃逸
 */
public class LocalVariableThreadSafe {

	/**
	 * 作为参数传递到方法，显然其他线程可以更改
	 *
	 * @param sb stringbuilder
	 */
	private void unsafe1(StringBuilder sb) {
		sb.append("111");
	}
	@Test
	public void testUnsafe1() {
		StringBuilder sb = new StringBuilder(20);
		new Thread(() -> unsafe1(sb), "T A").start();
		new Thread(() -> unsafe1(sb), "T B").start();
		System.out.println(sb.toString());
	}


	/**
	 * 作为返回值返回，显然其它线程可以更改
	 *
	 * @return sb
	 */
	private StringBuilder unsafe2() {
		StringBuilder sb = new StringBuilder();
		sb.append("1");
		return sb;
	}
	@Test
	public void testUnsafe2() {
		StringBuilder[] sbs = new StringBuilder[1];
		new Thread(() -> {
			sbs[0] = unsafe2();
			sbs[0].append("2");
			try { TimeUnit.MILLISECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
			sbs[0].append("4");
		}, "T A").start();
		new Thread(() -> {
			sbs[0] = unsafe2();
			sbs[0].append("3");
			try { TimeUnit.MILLISECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
			sbs[0].append("5");
		}, "T B").start();
		System.out.println(sbs[0].toString());
	}


}
