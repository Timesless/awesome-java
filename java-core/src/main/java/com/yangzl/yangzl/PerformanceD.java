package com.yangzl.yangzl;

import org.junit.jupiter.api.Test;

/**
 * @author yangzl
 * @date 2020/11/27 16:54
 * @desc 性能测试
 */

public class PerformanceD {
	
	/**
	 * 2020/11/27 对比 if，三目运算符，Math.max调用
	 * 
	 * if > 三目运算 > Math.max
	 *
	 * @param () v
	 * @return void
	 */
	@Test
	public void testPerformance() {

		int loopTimes = 400_000_00, max = -1;
		int[] nums = {1, 9, 20, 7, 5, 2, 3, 15, 43, 8, 4, 12, 19};
		long t1 = System.currentTimeMillis();
		for (int m = 0; m < loopTimes; ++m) {
			for (int i = 1, ln = nums.length; i < ln; ++i) {
				int gap = nums[i] - nums[i - 1];
				if (gap > max) {
					max = gap;
				}
			}
		}
		System.out.printf("if take : %d \n", System.currentTimeMillis() - t1);
		
		long t2 = System.currentTimeMillis();
		for (int m = 0; m < loopTimes; ++m) {
			for (int i = 1, ln = nums.length; i < ln; ++i) {
				int gap = nums[i] - nums[i - 1];
				max = Math.max(gap, max);
			}
		}
		System.out.printf("math.max take : %d \n", System.currentTimeMillis() - t2);
	}

}
