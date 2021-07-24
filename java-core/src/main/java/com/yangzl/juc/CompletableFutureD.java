package com.yangzl.juc;

import java.util.concurrent.CompletableFuture;

/**
 * @author yangzl
 * @date 2020/1/5 15:02
 *
 *  可组合式异步回调
 *
 * 	1. 返回值
 *  	1. runXxx「无返回值」
 *  	2. supplyXxx「有返回值」
 *
 * 	2. 完成时回调
 * 		whenComplete
 * 		whenComplteAsync「再开线程执行」
 * 		exceptionally
 * 		handle「可以改变返回结果」
 *
 * 	3. 编排
 * 		thenAccept「接收上一个线程的返回值，当前线程无返回值」
 * 		thenAcceptAsync
 *
 * 		thenApply「接收上一个线程的返回值，当前线程有返回值」
 * 		thenApplyAsync
 *
 * 		thenRun「不接受上一个线程的返回」
 * 		thenRunAsync
 *
 * 	4. 两个任务
 * 		both「组合的两个任务都完成」
 * 			runAfterBoth「组合两个 future，不获取结果不返回值」
 * 			runAfterBothAsync
 *
 * 			thenAcceptBoth「组合两个 future 的结果，自己无返回值」
 * 			thenAcceptAsync
 *
 * 			thenCombine「组合两个 future 的结果并返回自己处理的结果」
 *  		thenCombineAsync
 *
 *  	either「只要其中一个任务完成」
 *  		runAfterEither
 *  		runAfterEitherAsync
 *
 *  		acceptEither
 *  		acceptEitherAsync
 *
 *  		applyToEither
 *  		applyToEitherAsync
 *
 *  5. 多任务
 *
 *  	allOf
 *
 *  	anyOf
 *
 */
public class CompletableFutureD {

	/**
	 * handle 可以改变异常时的结果
	 *
	 * @throws Exception e
	 */
	public void handleTest() throws Exception {
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
			int i = 10 / 0;
			System.out.println("运行结果 = " + i);
			return i;
		}).handle((rs, throwable) -> throwable == null ? rs << 1 : 0);

		System.out.println(future.get());
	}


	public static void main(String[] args) throws Exception {
		// 无返回值的异步任务
		CompletableFuture.runAsync(
				() -> System.out.println(Thread.currentThread().getName() + "执行任务.."));
		// 有返回值的异步任务
		CompletableFuture<Integer> resultFutre = CompletableFuture.supplyAsync(() -> {
			System.out.println(Thread.currentThread().getName() + "执行任务...");
			// if (true) { throw new RuntimeException("rep"); }
			return 200;
		}).whenComplete((result, throwable) -> {
			if (null == throwable) {
				System.out.println("结果 = " + result);
			}
		}).exceptionally(throwable -> {
			throwable.printStackTrace();
			return 404;
		});
		System.out.println(resultFutre.get());
	}
}
