package com.yangzl.juc;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yangzl
 * @date 2020/1/5 11:25
 *
 * 线程池任务执行流程
 * 		1. 线程池创建时，准备 core 数量的核心线程
 * 		2. core 满，随后的任务先放入阻塞队列
 * 		3. 阻塞队列满，继续创建线程直到 max
 * 		4. max 也满了调用 RejectExecutionHandler
 * 		5. 在指定的的时间内，空闲的线程会从 max -> core
 *
 * 	new LinkedBlockingQueue<>() 容量默认是 Integer.MAX_VALUE
 */
public class ExecutorServiceD {
	
	/**
	 * @date 2020/8/24
	 * @desc
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
				new ArrayBlockingQueue<>(20));

		// 只提供Runnable
		Executor executor = new ThreadPoolExecutor(processor, processor << 1,
				60L,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(20));
	}
	
	/**
	 * 2020/11/29 无界队列时，max core 无用，永远只是core szie
	 */
	@Test
	public void testUnbundedQ() {
		AtomicInteger order = new AtomicInteger(0);
		ThreadFactory factory = r -> new Thread(r, "==pool- " + order.getAndIncrement() + "==");
		ThreadPoolExecutor p = new ThreadPoolExecutor(4, 8, 8, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), factory);
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
