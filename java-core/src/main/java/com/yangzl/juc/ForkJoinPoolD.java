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
 * 
 * 		这是fork/join的最佳使用方式
 * 			1. leftTask 递交给ForkJoinPool
 * 			2. rightTask 同步执行 compute
 * 				2.1 compute 递归的2个子任务分别为 leftTask，rightTask重复1，2步骤	
 */
public class ForkJoinPoolD extends RecursiveTask<Integer> {

	private static final int DIVIDE = 200_000;
	private final int start;
    private final int end;
    private int result;
	public ForkJoinPoolD(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * @Date: 2020/2/10
	 * @Desc:  1. 定义任务如何拆分
	 * 			2. 定义不能拆分时的计算逻辑
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
			return rightResult + leftTask.join();
		}
		result += IntStream.rangeClosed(start, end).sum();
		int tmp = 0;
		for(int i = 1; i < 40000; ++i) {
			for(int j = 1; j < 10000; ++j) {
				int mod = (i * j) % (i + j);
				tmp = i * j + i - j + mod;
			}
		}
		return result - tmp;
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism",
				String.valueOf(Runtime.getRuntime().availableProcessors() << 1));
		// new 时，直接传入 availableProcessors()，所以parallelism = 8
		ForkJoinPool newPool = new ForkJoinPool();
		System.out.println(newPool);
		ForkJoinPool pool = ForkJoinPool.commonPool();
		System.out.println(pool);
		long t1 = System.currentTimeMillis();
		ForkJoinPoolD task = new ForkJoinPoolD(0, 1000_000_000);
		/*
		 * submit 有返回值
		 * execute 无返回值
		 */
		Integer result = pool.invoke(task);
		System.out.printf("耗时： %d, 结果：%d", (System.currentTimeMillis() - t1), result);
		pool.shutdown();
	}
}
