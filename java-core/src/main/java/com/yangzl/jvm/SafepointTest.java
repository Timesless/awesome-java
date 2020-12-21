package com.yangzl.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author yangzl
 * @date 2020/11/18 13:33
 * @desc 
 * 
 * 	JVM在做GC之前要等所有的应用线程进入到安全点后VM线程才能分派GC任务
 * 	为了避免程序长时间无法进入safepoint，所以
 *  安全点所在位置: “长时间执行”的最明显特征就是指令序列的复用，例如方法调用、循环跳转、异常跳转
 *  
 *  "不调用别的方法或者是调用了方法但被内联" 
 *  调用别的方法或者调用了方法但被内联会产生安全点,如果这个循环里面没有做这些,也就是不存在任何安全点
 *  
 *  1. -XX:+UseCountedLoopSafepoints
 *  2. 修改循环变量为long
 *  
 *  主动中断式：
 *  当垃圾收集需要中断线程的时候，仅仅简单地设置一个标志位，各个线程执行过程时会不停地主动去轮询这个标志
 *  轮询的位置和安全点重合，另外还要加上所有创建对象和其它需要在堆分配内存的地方（避免没有足够内存分配对象）
 *  
 *  由于轮询会频繁出现，HotSpot使用内存保护陷阱的方式
 *  轮询操作被设计为一条汇编指令：test，当需要暂停用户线程时，将0x160100内存页置为不可读
 *  这样，当线程执行到test，如果需要GC，那么产生trap in，查找中断处理逻辑（挂起）
 *  0x01b6b62d: test 0x01b6d633: mov;
 *  OopMap{[60]=Oop off=460} ; 
 *  *invokeinterface size ; 
 *  - Client1::main@113 (line 23) ; 
 *  {virtual_call} ; 
 *  OopMap{[60]=Oop off=461} ; 
 *  *if_icmplt ; 
 *  - Client1::main@118 (line 23) ; 
 *  {poll}
 *  %eax,0x160100 0x50(%esp),%esi
 *
 *  所有线程都走到安全点，然后再判断是否需要GC？？ 难道周志明老师这里的话应该是这么理解的
 */

public class SafepointTest {
	private static final Logger logger = LoggerFactory.getLogger(SafepointTest.class);

	static Thread t1 = new Thread(() -> {
		while (true) {
			long start = System.currentTimeMillis();
			try { TimeUnit.MILLISECONDS.sleep(1000); } catch(InterruptedException e) { e.printStackTrace(); }
			long cost = System.currentTimeMillis() - start;
			// 按照正常情况,t1线程,大致上应是每隔1000毫秒左右,会输出一句话 我们使用 cost 来记录实际等待的时间
			(cost > 1010L ? System.err : System.out).printf("thread: %s, costs %d ms\n", Thread.currentThread().getName(), cost);
		}
	});

	static Thread t2 = new Thread(() -> {
		while (true) {
			// 下面是一个counted loop,单次循环末尾不会被加入安全点,整个for循环期执行结束之前,都不会进入安全点
			// 而我们知道,垃圾收集刚开始的时候需要先获取所有根节点,而根节点的获取依赖所有线程抵达安全点
			// 线程t1很简单,只需要隔1s就会进入安全点,之后,线程t1需要等到其他线程(t2)也进入到安全点
			// 而t2此时才刚刚是for循环的刚开始,所以需要消耗大量时间走完剩下的循环次数,这也就是为什么有时候t1实际cost时间多达5s的原因
			// 也就是gc发生时,要获取所有根节点,而想要获取根节点,就要所有线程抵达安全点,已经抵达的线程(t1)需要等待未抵达的线程(t2)到达安全点 然后才会继续垃圾收集的剩下内容
			
			// 10亿次循环大概4.5S左右结束
			for (int i = 1; i <= 600_000_000; i++) {
				boolean b = 1.0 / i == 0;
			}
			try { TimeUnit.MILLISECONDS.sleep(10); } catch(InterruptedException e) { e.printStackTrace(); }
		}
	});

	public static void main(String[] args) throws InterruptedException {
		t1.start();
		try { TimeUnit.MILLISECONDS.sleep(100); } catch(InterruptedException e) { e.printStackTrace(); }
		t2.start();
	}

}
