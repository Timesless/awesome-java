package com.yangzl.inaction8;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author yangzl
 * @date 2020/11/23 23:10
 *
 * 为了并行执行而设计的可拆分迭代器，spiltable iterator
 * 
 * 		调用 tryAdvance 等同于 iterator
 * 		调用 trySplit 返回一个 Spliterator
 */
public class WordSpliterator implements Spliterator<Character> {
	
	/** 要拆分的串 */
	private final String target;
	private int current = 0;
	
	public WordSpliterator(String target) {
		this.target = target;
	}

	@Override
	public boolean tryAdvance(Consumer<? super Character> action) {
		action.accept(target.charAt(current ++));
		return current < target.length();
	}

	@Override
	public Spliterator<Character> trySplit() {
		int ln;
		int curSize = (ln = target.length()) - current;
		if (curSize < 10) {
			return null;
		} else {
			int mid = (curSize >>> 1) + current;
			for (int splitPos = mid; splitPos < ln; ++ splitPos) {
				// Character.isWhiteSpace(ch)
				if (target.charAt(splitPos) == 32) {
					Spliterator<Character> spliterator = new WordSpliterator(target.substring(current, splitPos));
					current = splitPos;
					return spliterator;
				}
			}
		}
		return null;
	}

	@Override
	public long estimateSize() {
		return target.length() - current;
	}

	@Override
	public int characteristics() {
		return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
	}
	
	
	/**
	 * 2020/11/23 如何使用 可拆分迭代器呢，需要通过源自定义拆分为Stream
	 * 
	 * @param () v
	 */
	public static void main(String[] args) {
		// 神曲 地狱篇 第一句话
		String SENTENCE = "Nel    mezzo del cammin di nostra vita mi ritrovai in una selva oscura" +
				" che la dritta via era smarrita ";
		// true 代表创建并行流
		Stream<Character> stream = StreamSupport.stream(new WordSpliterator(SENTENCE), true);
		List<Character> list = stream.collect(Collectors.toList());
		System.out.println(list);
	}
}
