package com.yinhai.juc.pool;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: yangzl
 * @Date: 2020/1/5 15:02
 * @Desc: .. 异步回调
 **/
public class CompletableFutureD {

	public static void main(String[] args) throws Exception {
		CompletableFuture.runAsync(
				() -> System.out.println(Thread.currentThread().getName() + "执行任务..")
		);
		// 有返回值的执行任务
		CompletableFuture<Integer> resultFutre = CompletableFuture.supplyAsync(() -> {
			System.out.println(Thread.currentThread().getName() + "执行任务...");
			if (true) { throw new RuntimeException("rep"); }
			return 200;
		}).whenComplete((result, throwable) -> {
			if (null != throwable) { System.out.println("结果" + result); }
		}).exceptionally(throwable -> {
			throwable.printStackTrace();
			return 404;
		});
		System.out.println(resultFutre.get());
	}
}
