package com.yangzl.inaction8;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @author yangzl
 * @date 2020/11/23 21:51
 *
 * 并行流
 * 
 * 第七章 => 并行数据处理与性能
 * 
 * 1. Stream API 允许我们以声明式方式处理数据集
 * 2. parallelStream() 开启并行流
 * 3. 整个流水线的最后一次 sequential() / parallel() 决定采用顺序流还是并行流
 */

public class StreamD {

	// 并行流的线程来自Fork/Join Pool线程池
	@Test
	public void testParallelStream() {
		int processors = Runtime.getRuntime().availableProcessors();
		System.setProperty("java.util.concurrent.ForkJoinPool.common.Parallelism", String.valueOf(processors));
	}
	
	/**
	 * 2020/11/23 性能测试
	 */
	@Test
	public void testParallelPerfomance() {
		int target = 50_000_000;
		// 14
		long t1 = System.currentTimeMillis();
		long rs = 0;
		for (long i = 1; i <= target; ++i) {
			rs += i;
		}
		System.out.printf("迭代耗时：%d\n", System.currentTimeMillis() - t1);
		
		/*
		 * sum() 38
		 * reduce(0L, Long::sum) 40
		 */
		long t2 = System.currentTimeMillis();
		LongStream.rangeClosed(0, target).parallel().reduce(0L, Long::sum);
		System.out.printf("并行流耗时：%d\n", System.currentTimeMillis() - t2);
		
		/*
		 * sum() 17
		 * reduce(0L, Long::sum) 70
		 */
		long t3 = System.currentTimeMillis();
		LongStream.rangeClosed(0, target).reduce(0L, Long::sum);
		System.out.printf("顺序流耗时：%d\n", System.currentTimeMillis() - t3);
	}


	@Test
	public final void generateGg() {
		// 生成勾股数
		Stream<int[]> stream = IntStream.rangeClosed(1, 100).boxed()
				.flatMap(a -> IntStream.rangeClosed(a, 100)
						.filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
						.mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
				);
		stream.limit(5).forEach(arr -> Arrays.toString(arr));
	}

	
}
