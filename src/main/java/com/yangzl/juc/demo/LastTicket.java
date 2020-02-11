package com.yangzl.juc.demo;

/**
 * @Author: yangzl
 * @Date: 2020/1/4 20:20
 * @Desc: .. 在高内聚低耦合前提下
 * 				1. 线程操作资源类
 * 				2. 判断标识位（in a loop） 执行 唤醒
 * 				3. 防止虚假唤醒
 * 	caution: judge always be used in a loop
 * 	
 **/
public class LastTicket {
	// 无票出售，恰好作为标识位
	private int num = 0;
	
	public synchronized void incre() {
		try {
			/*
			 * 考虑以下情况
			 *  cpu调度 p11 num = 1
			 *  cpu调度 p21 进入 if 挂起
			 * 	cpu调度 c11 num = 0
			 *  cpu调度 p12 num = 1
			 * 	cpu调度 p21 因为if已经通过，那么在原位置向下执行 num = 2
			 * 	所以应该while循环，唤醒之后需再次判断
			 */
			// if (num != 0) { this.wait(); }
			while (num != 0) { this.wait(); }
			num++;
			System.out.println(Thread.currentThread().getName() + " : " +num);
			this.notifyAll();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void decre() {
		try {
			// if (num != 1) { this.wait(); }
			while (num != 1) { this.wait(); }
			num--;
			System.out.println(Thread.currentThread().getName() + " : " +num);
			this.notifyAll();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Date: 2020/1/4
	 * @Desc:  程序的入口，与Ticket无关
	 **/
	public static void main(String[] args) {
		LastTicket lastTicket = new LastTicket();
		new Thread(() -> { for (int x = 0; x < 10; ++x) lastTicket.incre();}, "p1").start();
		new Thread(() -> { for (int x = 0; x < 10; ++x) lastTicket.incre();}, "p2").start();
		new Thread(() -> { for (int x = 0; x < 10; ++x) lastTicket.decre();}, "c1").start();
		new Thread(() -> { for (int x = 0; x < 10; ++x) lastTicket.decre();}, "c2").start();
	}
}
