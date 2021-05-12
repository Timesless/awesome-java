package com.yangzl.jvm;

/**
 * @author yangzl
 * @date 2020/12/22 11:42
 *
 * 测试对象分配
 * 	概念上：对象都在堆分配，也有可能并不分配对象，只使用对象的属性（标量）在栈上分配
 * 
 * 	1. 多数情况下，对象分配在新生代的Eden区，当Eden区没有足够空间分配时，虚拟机将发起一次Minor GC
 * 	2. 少数情况下，大对象（超过一定阈值）直接分配到old
 * 	
 * 	-Xms20m
 * 	-Xmx20m
 * 	-Xmn10m
 * 	-XX:+PrintGCDetails
 * 	-XX:+UseSerialGC
 * 	
 * 	1. Serial 和 ParNew 收集器下大对象直接进入老年代
 * 	2. 长期存活的对象进入老年代 -XX:MaxTenuringThreshold=15
 * 	3. 动态对象年龄判断「Survivor中年龄相同对象的totalSize > totalSurvivor / 2，则大于等于该年龄的对象进入old generation」
 * 	4. 分配担保「老年代连续空间大于新生代对象总大小或历次晋升老年代对象平均大小就进行Minor GC，否则Full GC」
 */
public class AllocationTest {
	
	private static final int _1MB = 1024 * 1024;
	
	/**
	 * 程序执行完的结果是Eden占用4MB（被a4占用），Survivor空闲，老年代被占用6MB（被a1、2、3占用）
	 * 
	 * 这里运行结果恰好相反，new generation：6M，old generation 4M
	 * 
	 * @param () v
	 */
	public static void allocation() {
		byte[] a1, a2, a3, a4;
		a1 = new byte[_1MB << 1];
		a2 = new byte[_1MB << 1];
		a3 = new byte[_1MB << 1];
		// 出现一次 Minor GC
		a4 = new byte[_1MB << 2];
	}
	
	/**
	 * 2020/12/22 测试大对象分配直接进入老年代
	 * 	-XX:PretenureSizeThreshold=3m 只对serial和ParaNew新生代收集器有效
	 * 
	 * @param () v
	 */
	public static void blobAllocation() {
		byte[] a1;
		a1 = new byte[4 * _1MB];
	}

	/**
	 * 动态对象年龄判断 -XX:MaxTenuringThreshold=1
	 * 「Survivor中年龄相同对象的totalSize >= totalSurvivor / 2，则大于等于该年龄的对象进入old」
	 * 
	 * a1,a2,a3正常分配，a4分配时total> eden所以GC，由于动态年龄判断a1,a2,a3都进入old，此时new = 4234K，old=5166K
	 * 注释a2 new = 4234K，old=4873K，a1,a3进入old，所以new相同
	 * 
	 * @param () v
	 */
	public static void dynamicJudge() {
		byte[] a1, a2, a3, a4;
		// a1 + a2 >= Survivor / 2 = 512KB
		a1 = new byte[_1MB >> 2];
		a2 = new byte[_1MB >> 2];
		
		a3 = new byte[_1MB << 2];
		a4 = new byte[_1MB << 2];
		a4 = null;
		a4 = new byte[_1MB << 2];
	}
	
	/** 
	 * 分配担保：老年代连续空间 > 新生代对象总大小 或者 历次晋升老年代对象的平均大小则Minor GC否则Full GC
	 */
	public static void allocationPromotion() {
		byte[] a1, a2, a3, a4, a5, a6, a7;
		a1 = new byte[_1MB << 1];
		a2 = new byte[_1MB << 1];
		a3 = new byte[_1MB << 1];
		
		a1 = null;
		a4 = new byte[_1MB << 1];
		a5 = new byte[_1MB << 1];
		a6 = new byte[_1MB << 1];
		
		a4 = a5 = a6 = null;
		a7 = new byte[_1MB << 1];
	}

	public static void main(String[] args) {
		
		// allocation();
		// blobAllocation();
		// dynamicJudge();
		allocationPromotion();
		
	}

}
