package com.yangzl.juc.demo;

/**
 * @Author: yangzl
 * @Date: 2020/1/4 22:27
 * @Desc: .. 三个线程同时出售30张票
 **/
public class SaleTicket {

	private int tickets = 30;
	private synchronized void sale() {
		if (tickets > 0) { System.out.println(Thread.currentThread().getName() + " : " + (tickets--)); }
		this.notifyAll();
	}

	public static void main(String[] args) {
		SaleTicket ticket = new SaleTicket();
		new Thread(() -> { for (int x = 0; x < 30; ++x) ticket.sale(); }, "T1").start();
		new Thread(() -> { for (int x = 0; x < 30; ++x) ticket.sale(); }, "T2").start();
		new Thread(() -> { for (int x = 0; x < 30; ++x) ticket.sale(); }, "T3").start();
	}
}
