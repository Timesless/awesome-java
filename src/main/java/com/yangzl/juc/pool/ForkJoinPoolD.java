package com.yangzl.juc.pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * @Author: yangzl
 * @Date: 2020/1/5 13:55
 * @Desc: .. 分支合并框架
 **/
public class ForkJoinPoolD extends RecursiveTask<Integer> {

	private static final int DIVIDE = 1000_00;
	private int start, end, result;
	public ForkJoinPoolD(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * @Date: 2020/2/10
	 * @Desc:  定义任务如何拆分
	 **/
	@Override
	protected Integer compute() {
		if (end - start > DIVIDE) {
			int mid = (start + end) >> 1;
			ForkJoinTask<Integer> taskOne = new ForkJoinPoolD(start, mid);
			taskOne.fork();
			ForkJoinPoolD taskTwo = new ForkJoinPoolD(mid + 1, end);
			taskTwo.fork();
			return taskOne.join() + taskTwo.join();
		} else {
			result += IntStream.rangeClosed(start, end).sum();
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinPoolD task = new ForkJoinPoolD(0, 1000_000);
		ForkJoinTask<Integer> result = pool.submit(task);
		System.out.println(result.get());
		pool.shutdown();
	}
}
