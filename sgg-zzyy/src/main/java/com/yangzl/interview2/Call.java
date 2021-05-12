package com.yangzl.interview2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzl
 * @date 2021/3/20
 *
 * Callable 基本认识
 *
 * 		Runnable
 * 		Future
 * 		RunnableFuture
 * 		FutureTask
 */
@Slf4j
public class Call {

	/**
	 * get 会导致阻塞，请放在最后调用
	 * 多次运行一个 FutureTask 只会执行一次
	 */
	@Test
	public void testCallable() {
		Callable<Integer> task = () -> {
			System.out.println(Thread.currentThread().getName());
			return 1;
		};
		FutureTask<Integer> future = new FutureTask<>(task);
		new Thread(future, "T A").start();
		new Thread(future, "T B").start();
		try {
			int rs = future.get(1, TimeUnit.SECONDS);
			log.info("rs = {}", rs);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
