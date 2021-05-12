package com.yangzl.netty.time;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzl
 * @date 2020/12/26 15:37
 *
 * Netty时间轮调度算法
 * 
 * 	「tips：如果只是无限循环cpu负载过高，使用时间轮若任务在tickDuration以内完成那么Thread可以sleep」
 * 
 * wheel：时间轮数组
 * tickDuration： 事件循环多久拨动一次
 * timeouts：任务队列
 * HashedWheelBucket：时间轮从队列中加载的任务「链式结构、双向队列」
 */

public class WheelTimer {
	
	enum HashedWheelTimerInstance {
		
		/** 单例 */
		INSTANCE;
		
		private final MyHashedWheelTimer wheelTimer;
		
		HashedWheelTimerInstance() {
			this.wheelTimer = new MyHashedWheelTimer(r -> {
						Thread thread = new Thread(r);
						thread.setUncaughtExceptionHandler((t, err) -> {
							System.out.println(t.getName() + err.getMessage());
						});
						thread.setName("==HashedTimerWheelThread - ");
						return thread;
					}, 100, TimeUnit.MILLISECONDS, 100_00, false, 1024);
		}

		public MyHashedWheelTimer getWheelTimer() {
			return wheelTimer;
		}
	}
	
	
	static class MyWorker implements Runnable {
		@Override
		public void run() {
			
		}
	}
	
	/**
	 * @author yangzl 
	 * @desc 时间轮
	 */
	static class MyHashedWheelTimer {
		
		/** threadFactory 线程工厂，可定义线程名，异常处理器等 */
		private final ThreadFactory threadFactory;
		/** tickDuration 一次loop的运转时间 */
		private final long tickDuration;
		/** 时间单位 */
		private final TimeUnit unit;
		/** ticsPerWheel 每次loop处理多少任务 */
		private final int ticsPerWheel;
		/** leakDetection 是否内存泄漏检测 */
		private final boolean leakDetection;
		/** maxPendingTimeouts 最大等待任务数 */
		private final long maxPendingTimeouts;
		
		/** 工作线程 */
		private Runnable worker;
		
		public MyHashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit,
								  int ticsPerWheel, boolean leakDetection, long maxPendingTimeouts) {
			
			if (null == threadFactory) {
				throw new NullPointerException("threadFactory cant be null");
			}
			/*
			 * 事件循环一次不能太长时间或太短时间
			 * 且Windows调度必须为10ms的倍数
			 */
			if (tickDuration <= 9 || tickDuration > 500) {
				tickDuration = 100;
				unit = TimeUnit.MILLISECONDS;
			}
			tickDuration = tickDuration / 10 * 10;
			if (unit == null) {
				throw new NullPointerException("timeUnit cant be null");
			}
			if (ticsPerWheel <= 0) {
				ticsPerWheel = 100_00;
			}
			leakDetection = leakDetection || threadFactory.newThread(worker).isDaemon();
			// 队列任务数量不可太大
			if (maxPendingTimeouts <= 0 || maxPendingTimeouts > 0x200) {
				maxPendingTimeouts = 0x100;
			}
			
			this.threadFactory = threadFactory;
			this.tickDuration = tickDuration;
			this.unit = unit;
			this.ticsPerWheel = ticsPerWheel;
			this.leakDetection = leakDetection;
			this.maxPendingTimeouts = maxPendingTimeouts;
		}
	}

}
