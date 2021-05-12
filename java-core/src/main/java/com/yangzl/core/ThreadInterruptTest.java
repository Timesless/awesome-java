package com.yangzl.core;

import java.util.concurrent.TimeUnit;

/**
 * @author yangzl
 * @date 2020/12/28 13:51
 *
 * 线程中断测试
 * 
 * 	由于子线程在sleep()「阻塞状态，即时被中断也无法设置自己的中断标志位为true」
 * 	在main线程中断子线程，子线程由于不在运行状态而无法设置自己的中断标志中断位，在被catch之后继续执行while循环
 * 	但isInterrupted依旧返回false，所以无法跳出循环
 * 	
 * 	解决：catch之后主动中断自己 currentThread.interrupt()
 * 	
 * 	
 * 	************************************************************************************************* 
 * 	
 * 	当线程A运行时，线程B可以调用线程A的interrupt()来设置线程A的中断标志为true并立即返回。
 * 	仅仅是设置标志，线程A实际并没有被中断，它会继续往下执行。
 * 	如果线程A因为调用了wait系列函数、join方法或者sleep方法而被阻塞挂起，这时候若线程B调用线程A的interrupt()，
 * 	线程A会在调用这些方法的地方抛出InterruptedException异常而返回。
 * 	public void interrupt() {
 * 	    
 * 	}
 * 	
 * 	// 获取线程中断状态
 * 	public boolean isInterrupted() {
 * 	    
 * 	}
 * 	
 * 	// 获取线程中断状态并重置中断状态
 * 	public static boolean interrupted() {
 * 	    
 * 	}
 */
public class ThreadInterruptTest {


	public static void main(String[] args) {
		Runnable r = () -> {
				final Thread thread = Thread.currentThread();
				while (true) {
					if (thread.isInterrupted()) {
						break;
					}
					try { TimeUnit.MILLISECONDS.sleep(100); } catch(InterruptedException e) {
						e.printStackTrace();
						/*
						 * 1. 主动中断自己，在运行时能设置标志位为true，这样下次循环就会跳出
						 * 2. 使用try — catch 包裹，因为在sleep/join/wait期间被中断会抛出InterruptedException也可以正确结束
						 */ 
						thread.interrupt();
					}
				}
		};

		final Thread subThread = new Thread(r, "subThread");
		subThread.start();

		try { TimeUnit.SECONDS.sleep(2); } catch(InterruptedException e) { e.printStackTrace(); }
		
		/*
		 * 中断线程
		 * 当被中断线程「subThread」在运行时，该方法只是设置中断标志位，subThread实际并未中断
		 * 当被中断线程「subThread」调用了wait/join/sleep()在阻塞挂起状态时「并未获取cpu资源」
		 * 若其它线程「main」中调用了subThread的interrupt()，subThread会在wait/join/sleep处抛出InterruptedException而返回
		 */
		subThread.interrupt();
		
	}
}
