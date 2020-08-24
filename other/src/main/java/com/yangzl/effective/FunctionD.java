package com.effective;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Author: yangzl
 * @Date: 2020/3/3 11:38
 * @Desc: .. java.util.Function有43个接口
 *  函数式编程
 */
public class FunctionD {
	
	@Test
	public void testλ() {
		/*
		 * operator： 参数与返回类型相同
		 * 		UnaryOperator -> T apply(T t)
		 * 		BinaryOperator -> T apply(T t1, T t2)
		 * 
		 * 	Predicate<T>: 返回布尔类型 -> boolean test(T t)
		 * 
		 * 	Function<T, R> 参数与返回类型不同 -> R apply(T t)
		 * 		BiFunction<T, T, R> -> R apply(T t1, T t2)
		 * 
		 * 	Supplier<T> 无参 -> T get();
		 * 
		 * 	Consumer<T> 无返回值 -> void accept(T t);
		 */
	}

	@Test
	public void testMethodRef() {
		/**
		 * λ == 方法引用，选择较为简洁的一种方式
		 * 
		 * 静态方法引用		Integer::parseInt  == str -> Integer.parseInt(str)
		 * 实例::实例方法	Instant.now()::isAfter == Instant now = Instant.now(); t -> now.ifAfter(t)
		 * 实例::静态方法	String::toLowerCase == str -> str.toLowerCase()
		 * 类构造器引用		TreeMap<K, V>::new == () -> new TreeMap<K, V>()
		 * 数组构造器引用		int[]::new == len -> new int[len]
		 */
		Supplier<TreeMap<String, Object>> sup = TreeMap::new;
		TreeMap<String, Object> map = sup.get();
		
		// 等价
		Function<String, String> fun1 = str -> str.toLowerCase();
		// 参数是方法的调用者
		Function<String, String> fun2 = String::toLowerCase;
		System.out.println(fun1.apply("1222") == fun2.apply("1222"));

		Function<String, String> identity1 = Function.identity();	// return t -> t
		Function<String, String> identity2 = x -> x;
	}
	
	
	@Test
	public void testStream() {
		/*
		 * 流 & 流管道（中间操作）
		 * 
		 * 避免流处理char值
		 */
		// 721011081081113211911111410810033
		"Hello word!".chars().forEach(System.out::print);
		
		// 收集为string: strLen的键值对
		List<String> list = getValues();
		Map<String, Integer> map = list.stream()
				.collect(Collectors.toMap(String::toLowerCase, String::length, (ov, nv) -> nv));
		System.out.println(map);
		/*
		 * 该方法违反了可伸缩参数列表模式
		 * 下游收集器 Collectors.counting();
		 */
		Map<String, Long> collect = list.stream()
				.collect(Collectors.groupingBy(String::toLowerCase, HashMap::new, Collectors.counting()));
		System.out.println(collect);
		
		/*
		 * 并行流带来的性能收益在ArrayList, HashMap, HashSet, ConcurrentHashMap, 数组, int类型范围, long类型范围, 实例
		 * 并行的最佳操作是 reduce, min, max, sum, count, 短路操作 anyMatch, noneMatch, allMatch等
		 */
	}

	private List<String> getValues() {
		return Arrays.asList("hGllo", "zSz", "ccCs", "ddHz", "dZfs", "Zf**k");
	}
	
	
	@Test
	public void test3() {
		
	}
	public int majorityElement(int[] nums) {
		Map<Integer, Integer> res = new HashMap<>();
		for(int tmp : nums) {
			res.merge(tmp, 1, Integer::sum);
		}
		
		return res.values().stream().mapToInt(Integer::intValue).max().getAsInt();
	}
}
