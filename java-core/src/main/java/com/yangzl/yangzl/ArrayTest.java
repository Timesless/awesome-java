package com.yangzl.yangzl;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author yangzl
 * @date 2020/12/5 20:22
 * @desc 数组测试
 */

public class ArrayTest {
	
	@Test
	public void testArrayReflect() {
		final int[] o = (int[]) Array.newInstance(int.class, 10);
		System.out.println(Arrays.toString(o));
		Arrays.parallelPrefix(o, (prev, curr) -> prev + curr + 2);
		System.out.println(Arrays.toString(o));
		Arrays.setAll(o, idx -> idx + 1);
		System.out.println(Arrays.toString(o));
	}
	
	/**
	 * 2020/12/5 测试理解的 i2f d2l 指令, 精度丢失情况，符合预期
	 */
	@Test
	public void testFD() {
		// 2 << 23 = 8,388,608, 最多保存7位数的精度
		int i2 = 12345678;
		float f2 = i2;
		System.out.println(f2);
		
		// 所以即时是自动转型wide cast, 在8位时也会丢失精度
		int i = 123456789;
		float f = i;
		System.out.println(f);
		
		// 2 << 52 = 4,503,599,627,370,496, 最多保存16位数的精度
		long l = 1234567891234567L;
		double d = l;
		System.out.println(d);
		
		// 所以即时是自动转型, 在17位时也会丢失精度
		long l2 = 12345678912345678L;
		double d2 = l2;
		System.out.println(d2);
	}


}
