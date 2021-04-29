package com.yangzl.inaction8;

import lombok.NonNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.Repeatable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.function.LongBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author yangzl
 * @date 2020/11/24 19:19
 * @desc Java 8 In Action 附录 A, B, C, D
 */

public class Appendix {

	// =====================================================================================
	// A.1.1 可重复注解
	// =====================================================================================
	@Repeatable(Authors.class)
	@interface Author {
		String value();
	}

	@interface Authors {
		Author[] value();
	}

	/*
	 * 使用重复注解
	 */
	@Authors(
			{@Author("yangzl"), @Author("Doug Lea"), @Author("Download Kunth")}
	)
	static class Book {
	}

	/**
	 * 2020/11/24 这段代码要能正常工作的话，需要确保重复注解及它的容器都有<b>运行时保持策略</b>
	 *
	 * @param () ()
	 * @return void
	 */
	@Test
	public void testRepetableAnnotaion() {
		Author[] authors = Book.class.getDeclaredAnnotationsByType(Author.class);
		for (Author a : authors) {
			System.out.println(a.value());
		}
	}


	// =====================================================================================
	// A.1.2 类型注解
	// =====================================================================================
	/**
	 * 从Java8 开始，注解能应用在任何类型，包括：
	 * 	new、类型转换、instanceof检查、泛型类型参数、implements、throws子句
	 */
	@Test
	public void testAnnotation() {
		@NonNull String name = null;
		List<@NonNull String> list = new ArrayList<>();
	}


	// =====================================================================================
	// A.2 通用目标类型推断
	// =====================================================================================

	@Test
	public void testGenericDetected() {
		List<String> list = Collections.emptyList();
	}


	// =====================================================================================
	// B 类库更新
	// =====================================================================================

	@Test
	public void testLibUp() {
		Map<String, Integer> map = new HashMap<>(4);
		map.getOrDefault("Aston Martin", 0);
		map.computeIfAbsent("Aston Martin", String::length);

		List<Integer> list = IntStream.range(0, 5).boxed().collect(Collectors.toList());
		// map 会生成新元素，replaceAll则是替换它们
		list.replaceAll(x -> x << 1);
		System.out.println(list);

	}


	// =====================================================================================
	// B.1.3 Comparator
	// =====================================================================================
	// Comparator接口现在同时包含默认方法和静态方法，就不再像Collections提供为工具类了

	@Test
	public void testComparator() {
		Comparator<String> sCmp = Comparator.naturalOrder();
		Comparator<Integer> iCmp = Comparator.naturalOrder();
		Comparator<Map.Entry<String, Integer>> byKey = Map.Entry.comparingByKey();
		Comparator<Map.Entry<String, Integer>> byKeyCmp = Map.Entry.comparingByKey(sCmp);
		Comparator<Map.Entry<String, Integer>> byVal = Map.Entry.comparingByValue();
		Comparator<Map.Entry<String, Integer>> byValCmp = Map.Entry.comparingByValue(iCmp);
	}


	// =====================================================================================
	// B.2.1 原子操作
	// =====================================================================================
	// java.util.concurrent.atomic
	// Adder Accumulator

	@Test
	public void testAtomic() {
		AtomicInteger integer = new AtomicInteger();
		integer.getAndUpdate(x -> x + 2);
		integer.getAndAdd(2);
		integer.getAndAccumulate(2, Integer::sum);

		// 支持 + -
		LongAdder adder = new LongAdder();
		adder.add(2);
		adder.increment();
		long sum = adder.sum();

		// 支持四则运算
		LongBinaryOperator op = (x, y) -> x * y;
		LongAccumulator accumulator = new LongAccumulator(op, 1L);
		accumulator.accumulate(2);
		accumulator.get();
	}


	// =====================================================================================
	// B.2.2 ConcurrentHashMap
	// =====================================================================================

	@Test
	public void testConcurrentHashMap() {
		ConcurrentHashMap<String, Integer> concurrent = new ConcurrentHashMap<>(0x100);
		IntStream.rangeClosed(0, 2).boxed().forEach(i -> concurrent.put(i.toString(), i));

		// map, 键，值，Map.Entry

		// forEach
		concurrent.forEachKey(2, System.out::print);
		System.out.println();
		concurrent.forEachValue(2, System.out::print);
		System.out.println();
		concurrent.forEachEntry(2, System.out::print);
		System.out.println();

		// reduce
		// 001122
		String reduce = concurrent.reduce(2, (k, v) -> k + v.toString(), (prev, curr) -> prev.concat(curr));
		System.out.println("reduce: " + reduce);
		// 012
		String keys = concurrent.reduceKeys(2, (prev, curr) -> prev + curr);
		System.out.println(keys);
		// 3
		Integer vals = concurrent.reduceValues(2, Integer::sum);
		System.out.println(vals);

		// search TODO
		String search = concurrent.search(2, (k, v) -> k);
		System.out.printf("search map: %s \n", search);
		String searchKeys = concurrent.searchKeys(2, Function.identity());
		System.out.printf("search keys: %s \n", searchKeys);
		Integer searchValues = concurrent.searchValues(2, Function.identity());
		System.out.printf("search values: %d \n", searchValues);
	}


	// =====================================================================================
	// B.2.3 Arrays
	// =====================================================================================

	@Test
	public void testArrays() {
		int[] arr = new int[10];
		// 接收元素的索引，返回该索引对应的值
		Arrays.setAll(arr, x -> x + 1);
		System.out.println(Arrays.toString(arr));
		Arrays.parallelSetAll(arr, x -> x + 1);
		System.out.println(Arrays.toString(arr));

		Arrays.parallelPrefix(arr, (a, b) -> a + b);
		System.out.println(Arrays.toString(arr));
	}


	// =====================================================================================
	// B.5 Files
	// =====================================================================================

	@Test
	public void testFiles() {
		try (Stream<Path> walk = Files.walk(Paths.get("d:/test"))) {
			walk.forEach(file -> System.out.println(file.getFileName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
		try (Stream<String> lines = Files.lines(Paths.get("d:/gc.log"))) {
			lines.forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
		try (Stream<Path> od = Files.find(Paths.get("d:/test"), 2, (f, attrs) -> attrs.size() < 1000 )) {
			od.forEach(p -> System.out.println(p.getFileName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
