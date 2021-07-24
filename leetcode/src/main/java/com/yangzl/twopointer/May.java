package com.yangzl.twopointer;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * long time no see
 *
 * @author yangzl
 * @date 2021/5/20
 */
public class May {

	public List<String> topKFrequent(String[] words, int k) {
		Map<String, Integer> counter = new HashMap<>();
		for (String word : words) {
			counter.merge(word, 1, Integer::sum);
		}
		Comparator<Map.Entry<String, Integer>> comp = Map.Entry.<String, Integer>comparingByValue()
				.reversed()
				.thenComparing(Map.Entry::getKey);

		Comparator<Map.Entry<String, Integer>> cmp2 = (e1, e2) -> {
			int v1 = e1.getValue(), v2 = e2.getValue();
			return v1 == v2 ? e2.getKey().compareTo(e1.getKey()) : v1 - v2;
		};
		PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(comp);
		for (Map.Entry<String, Integer> tmp : counter.entrySet()) {
			pq.offer(tmp);
		}
		List<String> rs = new ArrayList<>(words.length);
		for (int i = 0; i < k; ++i) {
			rs.add(pq.poll().getKey());
		}
		return rs;
	}

	@Test
	public void testTopKFrequent() {
		String[] words = new String[]{"the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"};
		System.out.println(topKFrequent(words, 4));
	}

	static Pattern pattern = Pattern.compile("\\(([^()]*)\\)");
	/**
	 * 1190. 反转每对括号间的子串
	 *
	 * @param s str
	 * @return str
	 */
	public static String reverseParentheses(String s) {

		while (s.contains("(")) {
			Matcher m = pattern.matcher(s);
			if (m.find()) {
				String reverse = new StringBuilder(m.group(1)).reverse().toString();
				s = s.replaceAll(s, reverse);
			}
		}
		return s;
	}

	@Test
	public void testReverseParentheses() {
		System.out.println(reverseParentheses("(u(love)i)"));
	}

}
