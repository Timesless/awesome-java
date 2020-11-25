package com.yangzl.jvm;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author yangzl
 * @date 2020/11/23 10:58
 * @desc  jol - 对象内存查看
 * 
 * com.yangzl.jvm.MemoryInspect object internals:
 *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
 *       0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
 *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
 *       8     4        (object header)                           05 c1 00 f8 (00000101 11000001 00000000 11111000) (-134168315)
 *      12     4    int MemoryInspect.i                           0
 * Instance size: 16 bytes
 * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
 */

public class MemoryInspect {
	
	private int i;

	public static void main(String[] args) {
		MemoryInspect m = new MemoryInspect();
		System.out.println(ClassLayout.parseInstance(m).toPrintable());
		System.out.println("====================================================");
		/*
		 * 对象头
		 * 		MarkWord: 8B
		 * 		KlassPointer: 4B（开启指针压缩4B，未开启指针压缩8B。 -XX:+UseCompressedOops，默认开启）
		 * 		array：4B
		 * 实例数据
		 * 		oop * 3 = 4B * 3 = 12B（注意对象数组元素为oop，普通对象指针）
		 * padding
		 * 		4B
		 * 
		 * total：32B
		 */
		Object[] objs = new Object[3];
		System.out.println(ClassLayout.parseInstance(objs).toPrintable());
		System.out.println("====================================================");
		/*
		 * 对象头
		 * 		MarkWord：8B
		 * 		KlassPointer：4B
		 * 		array.length：4B
		 * 实例数据
		 * 		oop * 3 = 12B
		 * padding
		 * 		4B
		 * 
		 * total：32B
		 */
		Blog[] blogs = new Blog[3];
		System.out.println(ClassLayout.parseInstance(blogs).toPrintable());
		System.out.println("====================================================");
		/*
		 * 对象头
		 * 		MarkWord：8
		 * 		KlassPointer：4
		 * 		array.lenght:4
		 * 实例数据
		 * 		8(long) * 3 = 24
		 * padding
		 * 		0
		 * 
		 * total： 40B
		 */	
		long[] ints = new long[3];
		System.out.println(ClassLayout.parseInstance(ints).toPrintable());
	}
	
	public static class Blog {
		int i;
		int j;
	}
}


