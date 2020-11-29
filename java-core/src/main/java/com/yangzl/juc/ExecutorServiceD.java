package com.yangzl.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @Author: yangzl
 * @Date: 2020/1/5 11:25
 * @Desc: ..
 */
public class ExecutorServiceD {
	
	/**
	 * @Date: 2020/8/24
	 * @Desc: 
	 * Executor，只提供Runnable接口的任务
	 * ExecutorService：提供Runnable + Callable
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
	
	/**
	 * 2020/11/29 无界队列时，max core 无用，永远只是core szie
	 * 
	 * @param 
	 * @return void
	 */
	@Test
	public void testUnbundedQ() {
		ThreadPoolExecutor p = new ThreadPoolExecutor(4, 8, 8, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
		Runnable r = () -> {
			try { TimeUnit.SECONDS.sleep(4); } catch(InterruptedException e) { e.printStackTrace(); }
			System.out.println(Thread.currentThread().getName());
		};

		p.execute(r);
		p.execute(r);
		p.execute(r);
		p.execute(r);
		p.execute(r);
		p.execute(r);
		p.execute(r);
		p.execute(r);
		try { TimeUnit.SECONDS.sleep(10); } catch(InterruptedException e) { e.printStackTrace(); }
		p.shutdown();
	}
}
