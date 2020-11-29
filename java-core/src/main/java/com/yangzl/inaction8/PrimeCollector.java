package com.yangzl.inaction8;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author yangzl
 * @date 2020/11/23 19:17
 * @desc 质数分区器
 * 
 * Java 8 In Action => 创建自己的收集器
 * 		将前n个自然数按是否质数分区
 * 	
 * 	T 流中元素的类型
 * 	A 累计部分结果的对象类型（累加器的类型）
 * 	R 最终返回的类型
 * 	
 * 		此处T A R 对应：
 * 			Integer流
 * 			累加器类型Map<Boolean, List<Integer>>
 * 			结果类型Map<Boolean, List<Integer>>
 */

public class PrimeCollector implements
		Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {
	
	@Override
	public Supplier<Map<Boolean, List<Integer>>> supplier() {
		return () -> new HashMap<Boolean, List<Integer>>() {{
			put(Boolean.FALSE, new ArrayList<>());
			put(Boolean.TRUE, new ArrayList<>());
		}};
	}

	/**
	 * 2020/11/23 实现累加器
	 * 
	 * @param () 无参
	 * @return 累加器
	 */
	@Override
	public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {

		BiConsumer<Map<Boolean, List<Integer>>, Integer> consumer = (accumulator, n) -> {
			Boolean isPrime = isPrime2(accumulator.get(Boolean.TRUE), n);
			accumulator.get(isPrime).add(n);
		};
		return consumer;
	}

	/**
	 * 2020/11/23 为并行提供计算（如果并行可用的话，即：Characteristics.COCURRENT）
	 * 
	 * @param () 无参
	 * @return BinaryOperator
	 */
	@Override
	public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
		return (acc1, acc2) -> {
			acc1.get(Boolean.FALSE).addAll(acc2.get(Boolean.FALSE));
			acc1.get(Boolean.TRUE).addAll(acc2.get(Boolean.TRUE));
			return acc1;
		};
	}

	@Override
	public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
		return Function.identity();
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
	}

	// ========================================================================
	// 实现质数分区器 end
	// ========================================================================
	
	
	/**
	 * 2020/11/23 1. 判断当前数是否为质数
	 * 				当自然数很大时，占用内存很大，如何优化呢？
	 * 				我们只取到n的平方根即可，为此我们创建一个takeWhile函数优化空间
	 * 					请注意，这个takeWhile是即时的，如果需要一个延迟的takeWihle我们需要理解Stream API的实现	
	 * @param candidate 自然数
	 * @return boolean
	 */
	public static boolean isPrime1(int candidate) {
		int sqrtRoot = (int) Math.sqrt(candidate);
		// 与任何数模都不等于0是质数
		return IntStream.rangeClosed(2, sqrtRoot).noneMatch(i -> candidate % i == 0);
	}
	

	/**
	 * 2020/11/23 仅使用质数做除数， 如果除数本身不是质数，那么用不着测试了
	 * 			当测试某一个数字是否质数时，假设我们已经知道其它的质数列表
	 * 			空间优化后的函数：判断candidate是否为质数
	 * 			takeWhile是即时的，如果需要实现延迟的takeWhile需要了解Stream API的实现
	 * 
	 * @param  candidate 自然数
	 * @return boolean
	 */
	public static boolean isPrime2(List<Integer> primes, int candidate) {
		int sqrtRoot = (int)Math.sqrt(candidate);
		return takeWihle(primes, num -> num <= sqrtRoot)
				.stream()
				.noneMatch(i -> candidate % i == 0);
	}
	
	
	/**
	 * 2020/11/23 无需实现Collector<T, A, R>接口
	 * 			collect(Suppier<R> s, BiConsumer<R, <? super T>> acc, BiConsumer<R, R> combiner)这个函数即可实现
	 * 
	 * @param n 2 - n 的自然数
	 *          Suppiler Accumulator Combiner   
	 * @return Map
	 */
	public Map<Boolean, List<Integer>> partionBySAC(int n) {
		int sz = n >>> 3;
		Supplier<Map<Boolean, List<Integer>>> sup = () -> new HashMap<Boolean, List<Integer>>() {{
			put(Boolean.TRUE, new ArrayList<>(sz));
			put(Boolean.FALSE, new ArrayList<>(n - sz));
		}};
		BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator =
				(acc, candidate) -> acc.get(isPrime2(acc.get(true), candidate)).add(candidate);
		BiConsumer<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> combiner = (acc1, acc2) -> {
			acc1.get(true).addAll(acc2.get(true));
			acc1.get(false).addAll(acc2.get(false));
		};
		
		return IntStream.rangeClosed(2, n).boxed().collect(sup, accumulator, combiner);
	}

	/*
	 * 4 * 1000000 => min = 270
	 */
	public Map<Boolean, List<Integer>> partionPrime(int n) {
		return IntStream.rangeClosed(2, n).boxed().collect(new PrimeCollector());
	}

	/*
	 * 未优化的isPrime
	 * 4 * 1000000 => min = 320
	 */
	public Map<Boolean, List<Integer>> partionWithJdk(int n) {
		return IntStream.rangeClosed(2, n).boxed().collect(Collectors.partitioningBy(PrimeCollector::isPrime1));
	}
	
	
	// ================================================================
	// private
	// ================================================================

	/**
	 * 2020/11/23 只获取满足条件的列表元素，这样节省很大空间
	 *
	 * @param list 列表所有元素
	 * @param  p 谓词
	 * @return java.util.List<A>
	 */
	private static <A> List<A> takeWihle(List<A> list, Predicate<A> p) {
		int i = 0;
		for (A a : list) {
			if (!p.test(a)) {
				return list.subList(0, i);
			}
			++ i;
		}
		return list;
	}

	// ======================================================
	// test
	// ======================================================

	// 收集测试
	@Test
	public void testCollect() {
		Map<Boolean, List<Integer>> rs = IntStream.rangeClosed(2, 50)
				.boxed().collect(new PrimeCollector());
		System.out.printf("%d 以内的质数： %s \n非质数： %s \n",
				50, rs.get(Boolean.TRUE), rs.get(Boolean.FALSE));
	}
	
	// 性能测试
	@Test
	public void testCollectPerformance() {
		long min = 0x7fffffff;
		for (int i = 0; i < 4; ++i) {
			long start = System.currentTimeMillis();
			// partionWithCustomCollector(2_000_000);
			partionBySAC(2_000_000);
			// partionWithJdk(2_000_000);
			long take = System.currentTimeMillis() - start;
			if (take < min)
				min = take;
		}
		System.out.println("最少时间为: " + min);
	}

}
