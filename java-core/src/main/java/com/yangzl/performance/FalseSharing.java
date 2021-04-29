package com.yangzl.performance;

/**
 * @author yangzl
 * @date 2020/11/29 17:44
 * @desc 伪共享。与CPU处理高速缓存的方式有关
 * 
 * 		使用@Contented注解解决伪共享，@Contented注解会填充空白使每个字段占64K bytes
 * 		
 * 		// 4千万测试数据	
 * 		volatile long l;	// 1610 ms
 * 	    @Contended volatile long l;	// 800 ms
 * 	    
 * 	    
 * 	    严格来讲，伪共享未必涉及同步（或volatile）变量，CPU缓存中有任何数据写入了，其它保存了同样范围的缓存都必须invalid
 * 	    
 * 	    *********************************** 
 * 	    	然后，切记。 JMM要求数据只在同步原语(CAS, volatile, Lock, Synchronized)结束时必须写入主内存
 * 	    所以这种情况是最常见的
 * 	    ====================================
 * 	    这个栗子，如果4个long类型的值不是volatile定义的，那么这些值会被编译器放到寄存器中，
 * 	    不论多少个线程，测试都将很快执行完毕。 大约 30ms
 * 	    
 * 	    最好将数据移到局部变量中，稍后再保存起来避免伪共享
 */

public class FalseSharing extends Thread {
	
	public FalseSharing(Runnable r) {
		super(r);
	}
	
	private static final DataHolder dataHolder = new DataHolder();
	/**
	 * 请考虑DataHolder，每个属性都保存在相邻的位置
	 * 假设 l1 0xF20, 那么 l2 0xF28
	 * 当程序要操作l2时，会有一个大的内存（cache line，缓存行：一般同内存模组传输数据64K）载入CPU的某个核上
	 * 如果有第二个线程操作l3，则会加载同样的一段内存到另一个核的缓存行中
	 * 	当程序跟新本地缓存（当前核心）的某个值，其它核心在缓存一致性协议（MESI）下，由总线嗅探机制，得知该值被修改(M)
	 * 	那么其它核心必须废弃该缓存行，从主存中重新载入
	 */
	private static class DataHolder {
		public volatile long l1 = 0;
		public volatile long l2 = 0;
		public volatile long l3 = 0;
		public volatile long l4 = 0;
	}

	public static void main(String[] args) {
		int loops = 30_000_000, core = 4;
		FalseSharing[] clients = new FalseSharing[core];
		/*
		 * t1线程操作l1
		 * t2线程操作l2
		 * t3线程操作l3
		 * 这样每个线程修改，导致其他核心缓存失效
		 */
		clients[0] = new FalseSharing(() -> {
			for (long i = 0; i < loops; ++i) {
				dataHolder.l1 += i;
			}
		});
		clients[1] = new FalseSharing(() -> {
			for (long i = 0; i < loops; ++i) {
				dataHolder.l2 += i;
			}
		});
		clients[2] = new FalseSharing(() -> {
			for (long i = 0; i < loops; ++i) {
				dataHolder.l3 += i;
			}
		});
		clients[3] = new FalseSharing(() -> {
			for (long i = 0; i < loops; ++i) {
				dataHolder.l4 += i;
			}
		});
		
		
		// ==========================================
		
		long time = System.currentTimeMillis();
		for (Thread t : clients) {
			t.start();
		}
		for (Thread t : clients) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("4线程耗时：%d", System.currentTimeMillis() - time);
	}

}
