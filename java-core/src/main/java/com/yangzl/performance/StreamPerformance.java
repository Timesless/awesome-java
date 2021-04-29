package com.yangzl.performance;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yangzl
 * @date 2020/12/2 22:39
 * @desc Stream API 性能测试
 */
public class StreamPerformance {
	
	@Test
	public void testStream() {
		List<List<Integer>> lists = new ArrayList<>(0x100);
		for (int i = 0; i < 0x1000; ++i) {
			List<Integer> list = new ArrayList<>(10000);
			for (int j = 0; j < 6000; ++j) {
				list.add(StdRandom.uniform(100_000_0));
			}
			lists.add(list);
		}
		/*
		 * t1: 250 ms
		 * t2: 2973
		 * t3: 445
		 * t4: 310
		 */
		long t1 = System.currentTimeMillis();
		int[] arr1 = lists.stream().flatMap(List::stream).filter(Objects::nonNull).mapToInt(Integer::intValue).toArray();
		System.out.printf("耗时: %d \n", System.currentTimeMillis() - t1);
		
		long t2 = System.currentTimeMillis();
		arr1 = lists.stream().flatMapToInt(list -> list.stream().filter(Objects::nonNull).mapToInt(Integer::intValue)).toArray();
		System.out.printf("耗时: %d \n", System.currentTimeMillis() - t2);

		long t3 = System.currentTimeMillis();
		arr1 = lists.stream().flatMap(List::stream).filter(Objects::nonNull).mapToInt(Integer::valueOf).toArray();
		System.out.printf("耗时: %d \n", System.currentTimeMillis() - t3);

		long t4 = System.currentTimeMillis();
		arr1 = lists.stream().flatMap(List::stream).filter(Objects::nonNull).mapToInt(Integer::new).toArray();
		System.out.printf("耗时: %d \n", System.currentTimeMillis() - t4);
	}

}
