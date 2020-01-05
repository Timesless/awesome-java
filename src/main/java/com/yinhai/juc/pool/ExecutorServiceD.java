package com.yinhai.juc.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: yangzl
 * @Date: 2020/1/5 11:25
 * @Desc: ..
 **/
public class ExecutorServiceD {

	public static void main(String[] args) {
		final int processor = Runtime.getRuntime().availableProcessors();
		ExecutorService service = new ThreadPoolExecutor(processor,
				processor << 1,
				60L,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(20));
	}
}
