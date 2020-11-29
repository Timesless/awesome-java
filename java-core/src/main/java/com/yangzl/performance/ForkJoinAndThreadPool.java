package com.yangzl.performance;

import edu.princeton.cs.algs4.StdRandom;

import java.util.concurrent.*;

/**
 * @author yangzl
 * @date 2020/11/28 21:27
 * @desc
 * 
 * 
 * 结果: 49995349, 线程池花费时间：50 
 * 结果: 49995349, forkjoin花费时间：80 
 * 
 * 	8 core 线程池花费时间稳定，为 50 ms 左右
 *  fork/join pool 在1亿个元素，拆分为1百万，2百万，5百万性能相近，都在70 ~ 90 ms之间
 *  
 *  当计算密集时，且其中有少数部分任务执行时间较长，threadpool的影响很大，而fork / join 由于工作窃取受影响很小
 *  
 *  加入dummy的计算之后， forkjoin计算恒定使用25% CPU， 这是为何啊
 *  		？？？？？？？？？？？？？？？？
 *  		导致速度永远是threadpool的 1 / 4	
 */

public class ForkJoinAndThreadPool {

	private final double[] d;
	
	public ForkJoinAndThreadPool(double[] d) {
		this.d = d;
	}

	/**
	 * 2020/11/29 执行计算
	 * 
	 * @param l start
	 * @param r end
	 * @return int < 0.5的数量
	 */
	private int buss(int l, int r) {
		int count = 0;
		for (int i = l; i < r; ++i) {
			if (d[i] < 0.5) {
				++count;
			}
			for(int j = 0; j < d.length - i; ++j) {
				for(int k = 0; k < 20; ++k) {
					double dummy = j * k + i;
					d[i] = dummy > 1_000_000_00 ? dummy : d[i];
				}
			}
		}
		return count;
	}
	
	/*
	 * fork join 栗子
	 */
	private class ForkJoinT extends RecursiveTask<Integer> {
		
		private final int start;
		private final int end;
		private final int threshold = 20;
		public ForkJoinT(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		protected Integer compute() {
			int l = start, r = end, s = threshold;
			if (l - r <= threshold) {
				return buss(l, r);
			} else {
				// 分解
				int m = l + (r - l >>> 1);
				// 左边任务异步，右边任务同步计算
				ForkJoinT left = new ForkJoinT(l, m);
				left.fork();
				ForkJoinT right = new ForkJoinT(m, r);
				int rs = right.compute();
				return rs + left.join();
			}
		}
	}
	
	/*
	 * 线程池 栗子
	 */
	private class ThreadPoolT implements Callable<Integer> {
		
		private final int start;
		private final int end;
		public ThreadPoolT(int start, int end) {
			this.start = start;
			this.end = end;
		}
		
		@Override
		public Integer call() {
			int l = start, r = end, count = 0;
			count = buss(l, r);
			return count;
		}
	}

	

	/*
	 *  8 核线程池测试
	 */
	static int poolExec(int core, ForkJoinAndThreadPool client) {
		int per = client.d.length >>> 3;
		ThreadPoolExecutor pool = new ThreadPoolExecutor(core, core, core, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
		Future[] fs = new Future[core];
		for (int i = 0; i < core; ++i) {
			int start = i * per, end = (i + 1) * per;
			ThreadPoolT t = client.new ThreadPoolT(start, end);
			fs[i] = pool.submit(t);
		}
		int rs = 0;
		for (Future<Integer> f: fs) {
			try {
				rs += f.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		pool.shutdown();
		return rs;
	}
	
	public static int forkjoinExec(ForkJoinAndThreadPool client) {
		ForkJoinPool pool = new ForkJoinPool(8);
		ForkJoinT task = client.new ForkJoinT(0, client.d.length);
		int joinrs = pool.invoke(task);
		pool.shutdown();
		return joinrs;
	}


	public static void main(String[] args) {
		int core = 8;
		double[] d = new double[40_000];
		for (int i = 0, ln = d.length; i < ln; ++i) {
			d[i] = StdRandom.uniform();
		}
		ForkJoinAndThreadPool client = new ForkJoinAndThreadPool(d);
		
		long t1 = System.currentTimeMillis();
		int poolrs = poolExec(core, client);
		System.out.printf("结果: %d, 线程池花费时间：%d \n", poolrs, System.currentTimeMillis() - t1);
		
		long t2 = System.currentTimeMillis();
		int joinrs = forkjoinExec(client);
		System.out.printf("结果: %d, forkjoin花费时间：%d \n", joinrs, System.currentTimeMillis() - t2);
	}
	
}
