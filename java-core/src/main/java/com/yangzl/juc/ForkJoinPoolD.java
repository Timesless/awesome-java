package com.yangzl.juc;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * @Author: yangzl
 * @Date: 2020/1/5 13:55
 * @Desc: .. 分支合并框架
 * 
 * RecursiveTask 有返回值
 * RecursiveAction 无返回值
 */
public class ForkJoinPoolD extends RecursiveTask<Integer> {

	private static final int DIVIDE = 1000_00;
	private final int start;
    private final int end;
    private int result;
	public ForkJoinPoolD(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * @Date: 2020/2/10
	 * @Desc:  定义任务如何拆分
	 */
	@Override
	protected Integer compute() {
		if (end - start > DIVIDE) {
			int mid = (start + end) >> 1;
			ForkJoinTask<Integer> leftTask = new ForkJoinPoolD(start, mid);
			// 利用ForkJoinPool中另一个线程异步执行创建的子任务
			leftTask.fork();

			// 另外一个任务则由自己执行
			ForkJoinPoolD rightTask = new ForkJoinPoolD(mid + 1, end);
			int rightResult = rightTask.compute();
			// taskTwo.fork();
			return leftTask.join() + rightResult;
		} 
		return (result += IntStream.rangeClosed(start, end).sum());
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism",
				String.valueOf(Runtime.getRuntime().availableProcessors() << 1));
		// new 时，直接传入 availableProcessors()，所以parallelism = 8
		ForkJoinPool newPool = new ForkJoinPool();
		System.out.println(newPool);
		ForkJoinPool pool = ForkJoinPool.commonPool();
		System.out.println(pool);
		ForkJoinPoolD task = new ForkJoinPoolD(0, 1000_000);
		/*
		 * submit 有返回值
		 * execute 无返回值
		 */
		ForkJoinTask<Integer> result = pool.submit(task);
		System.out.println(result.get());
		pool.shutdown();
	}
}
