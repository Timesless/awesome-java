package com.yangzl;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;

/**
 * @author yangzl
 * @date 2020/3/31 17:05
 * 
 *  .. 不适合归类的测试
 */
public class Daily {
	
	/**
	 * @date 2020/3/31
	 *  归并排序
	 */
	public void mergeSort(int[] nums, int left, int right) {
		if (left == right) return;
		int mid = left + (right - left >>> 1);
		mergeSort(nums, left, mid);
		mergeSort(nums, mid + 1, right);
		// 合并
		merge(nums, left, mid, right);
	}
	public void merge(int[] nums, int L, int M, int R) {
		int[] tmp = new int[R - L + 1];
		// 左右指针，tmp数组指针
		int i = L, j = M + 1, p = 0;
		while (i <= M && j <= R) {
			if (nums[i] <= nums[j])
				tmp[p++] = nums[i++];
			else
				tmp[p++] = nums[j++];
		}
		while (i <= M) tmp[p++] = nums[i++];
		while (j <= R) tmp[p++] = nums[j++];
		// 复制回原数组
		for (int k = 0; k < tmp.length; ++k)
			nums[L + k] = tmp[k];
	}
	@Test
	public void testMergeSort() {
		int[] arr = {1, 5, 3, 4, 2, 8, 6};
		mergeSort(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));
	}

	// =======================================================================
	// 2020/4/3
	// 字符串转整数。 脑瓜子嗡嗡的
	// =======================================================================
	/**
	 * 2020/12/21 字符串转整数
	 * 
	 * @param str 源串
	 * @return int
	 */
	public int myAtoi(String str) {
		int ln;
		if ((ln = str.length()) == 0) return 0;
		str = str.trim();
		if ((ln = str.length()) == 0) return 0;
		char fc = str.charAt(0);
		if (fc != '-' && fc != '+' && (fc < 48 || fc > 57)) return 0;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < ln; ++i) {
			int cln = builder.length();
			char c = str.charAt(i);
			// 如果0后面的数字不是数字，那么返回0
			if (c == 48 && i < ln - 1 && (str.charAt(i + 1) < 48 || str.charAt(i + 1) > 57)) {
				builder.append(0);
				break;
			}
			// 如果已经有过数字，那么后面的字符只要不是数字跳出循环
			if (cln != 0 && (c < 48 || c > 57)) break;
			// 当前builder为空，或者有一个符号位
			if (cln == 0 || (cln == 1 && (builder.indexOf("-") >=0 || builder.indexOf("+") >= 0)))
				// 0字符跳过
				if (c == 48)
					continue;
			// 符号 或者数字都可以拼接
			if (c == '-' || c == '+' || (c > 47 && c < 58))
				builder.append(c);
		}
		int idx;
		// 如果有+号，把它剔除掉
		if ((idx = builder.indexOf("+")) >= 0) builder.deleteCharAt(idx);
		int bln = builder.length();
		// 在11位以内
		if (bln > 0 && bln < 12) {
			char bfc = builder.charAt(0);
			if (bfc != '-') {
				long rs = Long.parseLong(builder.toString());
				return rs > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)rs;
			} else if (bln > 1) {
				long rs = Long.parseLong(builder.deleteCharAt(0).toString());
				return rs > Integer.MAX_VALUE ? Integer.MIN_VALUE : -1 * (int) rs;
			}
		} else if (bln > 11) {
			// 大整数
			return builder.indexOf("-") >= 0 ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		}
		return 0;
	}
	
	/**
	 * @date 2020/4/3
	 *   位运算实现 / 
	 */
	public int devide(int a, int b) {
		int divisor = Math.abs(a), dividend = Math.abs(b);
		int quotient = 0;
		for (int i = 31; i >= 0; --i) {
			// 最接近dividend的数
			if ((divisor >>> i) >= dividend) {
				quotient += 1 << i;
				divisor -= dividend << 1;
			}
		}
		// 确定商的符号
		return (a ^ b) > 0 ? quotient : (~quotient + 1);
	}
	@Test
	public void testDevide() {
		System.out.println(devide(5, 2));
	}

	// =======================================================================
	// 
	// =======================================================================

	List<List<Integer>> res = new ArrayList<>();
	public List<List<Integer>> findSolution(BiFunction<Integer, Integer, Integer> customfunction, int z) {
		if (z == 0) return res;
		int x = 1, y = z;
		while (x <= z && y > 0) {
			int m, n;
			int trs = customfunction.apply((m = x), (n = y));
			if (trs == z) {
				res.add(new ArrayList<Integer>() {{add(m); add(n);}});
				++ x;
				-- y;
			} else if (trs > z)
				-- y;
			else ++ x;
		}
		return res;
	}
	@Test
	public void testFindSolution() {
		BiFunction<Integer, Integer, Integer> function = (x, y) -> x + y;
		System.out.println(findSolution(function, 5));
	}
	

	/**
	 * @date 2020/4/4
	 *   42 Hard. 接雨水
	 */
	public int trap(int[] height) {
		if (height.length == 0) return 0;
		// x, y双指针
		int rs = 0, leftMax = 0, rightMax = 0, x = 0, y = height.length - 1;
		while (x < y) {
			if (height[x] < height[y]) {
				if (height[x] > leftMax)
					leftMax = height[x];
				else 
					rs += leftMax - height[x];
				++ x;
			} else {
				if (height[y] > rightMax)
					rightMax = height[y];
				else 
					rs += rightMax - height[y];
				-- y;
			}
		}
		return rs;
	}
	@Test
	public void testTrap() {
		System.out.println(trap(new int[] {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
	}


	// =======================================================================
	// 
	// =======================================================================

	public int minCount(int[] coins) {
		int rs = 0;
		for (int i = 0; i < coins.length; ++ i) 
			rs += (coins[i] & 1) == 1 ? (coins[i] >>> 1) + 1 : coins[i] >>> 1;
		return rs;
	}

	public String reformat(String s) {
		int len = s.length();
		if (len < 2) return s;
		int charCount = 0, digitCount = 0;
		List<Character> chars = new ArrayList<>(len);
		List<Character> digits = new ArrayList<>(len);
		for (char c : s.toCharArray()) {
			if (c >= 48 && c <= 57) {
				++ digitCount;
				digits.add(c);
			} else {
				++ charCount;
				chars.add(c);
			}
		}
		if (Math.abs(charCount - digitCount) > 1) return "";
		StringBuilder builder = new StringBuilder(len);
		int i = 0, j = 0;
		if (charCount > digitCount) {
			builder.append(chars.get(0));
			i = 1;
			while (i < charCount && j < digitCount)
				builder.append(digits.get(j++)).append(chars.get(i++));
		} else if (digitCount > charCount) {
			builder.append(digits.get(0));
			j = 1;
			while (i < charCount && j < digitCount)
				builder.append(chars.get(i++)).append(digits.get(j++));
		} else {
			for (int m = 0; m < charCount; ++m)
				builder.append(digits.get(m)).append(chars.get(m));
		}
		return builder.toString();
	}
	@Test
	public void test2() { System.out.println(reformat("c")); }

	/**
	 * 2020/5/28 DCP
	 * @param orders 菜单
	 * @return List
	 */
	public List<List<String>> displayTable(List<List<String>> orders) {
		List<List<String>> rs = new ArrayList<>();
		int sz = orders.size();
		Set<String> tables = new HashSet<>();
		Set<String> titles = new HashSet<>(sz + 1);
		Map<String, Integer> map = new HashMap<>();
		for (List<String> order : orders) {
			String t = order.get(1);
			String n = order.get(2);
			titles.add(n);
			tables.add(t);
			map.merge(n + t, 1, Integer::sum);
		}
		List<String> collect = new ArrayList<>(titles);
		collect.sort(Comparator.naturalOrder());
		collect.add(0, "Table");
		rs.add(collect);
		List<String> tabless = new ArrayList<>(tables);
		tabless.sort(Comparator.comparingInt(Integer::valueOf));
		for (int x = 0; x < tabless.size(); ++ x) {
			List<String> list = new ArrayList<>();
			String table = tabless.get(x);
			list.add(table);
			for (int i = 1; i < collect.size(); ++i) {
				Object o = map.get(collect.get(i));
				if (o == null)
					list.add("0");
				else
					list.add(o.toString());
			}
			rs.add(list);
		}
		return rs;
	}

	

	
	// =======================================================================
	// 
	// =======================================================================


	// =======================================================================
	// 
	// =======================================================================
}



