package com.yangzl.juc.pool;

import java.util.concurrent.*;

/**
 * @Author: yangzl
 * @Date: 2020/1/5 11:25
 * @Desc: ..
 **/
public class ExecutorServiceD {
	
	/**
	 * @Date: 2020/8/24
	 * @Desc: 
	 * Executor，只提供Runnable接口的任务
	 * ExecutorService：提供Runnable与Callable
	 * SecheduledExecutorService：可调度式执行器
	 */
	public static void main(String[] args) {
		final int processor = Runtime.getRuntime().availableProcessors();
		// 提供Callable与Runnable
		ExecutorService service = new ThreadPoolExecutor(processor,
				processor << 1,
				60L,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(20));

		// 只提供Runnable
		Executor executor = new ThreadPoolExecutor(processor, processor << 1,
				60L,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(20));
		
	}
}
