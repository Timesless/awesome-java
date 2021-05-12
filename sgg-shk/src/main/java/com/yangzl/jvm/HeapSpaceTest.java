package com.yangzl.jvm;

/**
 * @author yangzl
 * @date 2021/3/14
 *
 * 测试堆空间初始值与最大值，注意：runtime.maxMemory() = eden + s0 / s1 + 老年代
 *
 * 	-Xms1000m
 * 	-Xmx1000m
 * 	-Xmn400m
 */
public class HeapSpaceTest {

	/**
	 * initial = max = 950m
	 * 年轻代每次只能选择 eden + s0 / s1 = 300m + 50m「难道默认是 6:1:1，或者说 jvm 自动调整了」
	 *
	 * @param args args
	 */
	public static void main(String[] args) {
		final Runtime runtime = Runtime.getRuntime();
		long inital = runtime.totalMemory() / 1024 / 1024;
		long max = runtime.maxMemory() / 1024 / 1024;
		System.out.println("初始内存 = " + inital + " m");
		System.out.println("最大内存 = " + max + " m");
	}

}
