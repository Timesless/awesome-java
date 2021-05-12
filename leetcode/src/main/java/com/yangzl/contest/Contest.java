package com.yangzl.contest;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yangzl
 * @date 2020/4/26 09:39
 */
public class Contest {

	// =======================================================================
	// 188.1
	// =======================================================================

	public List<String> buildArray(int[] target, int n) {
		
		List<String> rs = new ArrayList<>();
		Set<Integer> collect = Arrays.stream(target)
				.mapToObj(Integer::valueOf).collect(Collectors.toSet());
		int min = Math.min(target[target.length - 1], n);
		for (int i = 1; i <= min; ++i) {
			rs.add("Push");
			if (!collect.contains(i))
				rs.add("Pop");
		}
		return rs;
	}
	
	// =======================================================================
	// 188.2
	// =======================================================================

	public int countTriplets(int[] arr) {
		if (arr.length < 2) return 0;
		int ln = arr.length, count = 0;
		for (int i = 0; i < ln - 1; ++i)
			for (int j = i + 1; j < ln - 1; ++j)
				for (int k = j; k < ln; ++k) {
					int a = arr[i] ^ arr[i + 1];
					int x = i + 1;
					while (x < j - 1) {
						a ^= arr[x];
						++ x;
					}
					int b = arr[j] ^ arr[j + 1];
					int y = j + 1;
					while (y < k) {
						b ^= arr[y];
						++ y;
					}
					if (a == b)
						++ count;
				}
		
		return count;
	}

	// =======================================================================
	// 186 正确括号的最大层数
	// =======================================================================

	public int maxDepth(String s) {
		int deepth = 0, maxDeepth = 0;
		Deque<Character> stack = new LinkedList<>();
		for (char c : s.toCharArray()) {
			if (c == '(') {
				++ deepth;
				maxDeepth = Math.max(maxDeepth, deepth);
				stack.push(c);
			} else if (c == ')') {
				--deepth;
				stack.pop();
			}
		}
		return maxDeepth;
	}


	/**
	 * 2020/11/25 上升下降的字符串
	 * 
	 * @param s source
	 * @return java.lang.String
	 */
	public String sortString(String s) {
		if (s.length() == 1) {
			return s;
		}
		TreeMap<Character, Integer> map = new TreeMap<>();
		for (char c : s.toCharArray()) {
			map.merge(c, 1, Integer::sum);
		}
		int idx = 0, ln = s.length();
		StringBuilder builder = new StringBuilder(ln);
		while (idx < ln) {
			for (Map.Entry<Character, Integer> ascNext : map.entrySet()) {
				Integer ascValue = ascNext.getValue();
				if (ascValue > 0) {
					builder.append(ascNext.getKey());
					ascNext.setValue(ascValue - 1);
					idx++;
				}
			}
			for (Map.Entry<Character, Integer> descNext : map.descendingMap().entrySet()) {
				Integer descValue = descNext.getValue();
				if (descValue > 0) {
					builder.append(descNext.getKey());
					descNext.setValue(descValue - 1);
					idx++;
				}
			}
		}
		return builder.toString();
	}
	@Test
	public void testSortString() {
		System.out.println(sortString("aaaabbbbcccdd"));
	}
	

}
