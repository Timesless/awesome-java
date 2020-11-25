package com.yangzl.contest;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yangzl
 * @date 2020/10/10 15:08
 * @desc 牛客 ACM
 */
public class Main {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int strNum = Integer.parseInt(scanner.nextLine());
		String[] strs = new String[strNum];
		int i = 0;
		while (scanner.hasNextLine()) {
			String str = scanner.nextLine();
			if (str.isEmpty()) {
				break;
			}
			strs[i++] = str;
		}
		for (int j = 0; j < strNum; ++j) {
			strs[j] = strs[j].replaceAll("([0-9a-zA-Z])\\1{2,}", "$1$1")
					.replaceAll("(\\w)\\1(\\w)\\2", "$1$1$2");
		}
		System.out.println(Arrays.toString(strs));
	}
	
	private int tryf() {
		int x = 0;
		try {
			return ++x;
		} finally {
			return ++x;
		}
	}
	@Test
	public void test1() {
		System.out.println(tryf());
	}


	public int maxLengthBetweenEqualCharacters(String s) {
		Map<Character, Integer> frq = new HashMap<>(s.length());

		for (char c : s.toCharArray()) {
			frq.merge(c, 1, Integer::sum);
		}
		List<Character> list = frq.entrySet().stream()
				.filter(entry -> entry.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
		if (list.isEmpty()) {
			return -1;
		}
		int max = -1;
		for (char c : list) {
			max = Math.max(max, s.lastIndexOf(c) - s.indexOf(c) - 1) ;
		}
		return max;
	}


	public char slowestKey(int[] releaseTimes, String keysPressed) {
		int i = 1;
		List<Character> c = new ArrayList<>(releaseTimes.length);
		c.add(keysPressed.charAt(0));
		int max = releaseTimes[0];
		while (i < releaseTimes.length) {
			int diff = releaseTimes[i] - releaseTimes[i - 1];
			if (diff > max) {
				max = diff;
				c.clear();
				c.add(keysPressed.charAt(i));
			} else if (diff == max) {
				c.add(keysPressed.charAt(i));
			}
			++i;
		}
		c.sort(Comparator.naturalOrder());
		return c.get(c.size() - 1);
	}
	@Test
	public void testSlowestKey() {
		int[] arr = {16, 23, 36, 46, 62};
		System.out.println(slowestKey(arr, "spuda"));
	}


	public List<Boolean> checkArithmeticSubarrays(int[] nums, int[] l, int[] r) {
		List<Boolean> rs = new ArrayList<>(l.length);
		List<int[]> todo = new ArrayList<>(l.length);
		for (int i = 0; i < l.length; ++i) {
			int[] arr = Arrays.copyOfRange(nums, l[i], r[i] + 1);
			Arrays.sort(arr);
			System.out.println(Arrays.toString(arr));
			todo.add(arr);
		}
		for (int i = 0, sz = todo.size(); i < sz; ++i) {
			int[] arr = todo.get(i);
			if (arr.length < 3) {
				rs.add(Boolean.TRUE);
				continue;
			}
			int diff = arr[1] - arr[0];
			boolean flag = true;
			for (int x = 1, y = 2; y < arr.length; ++x, ++y) {
				if (diff != arr[y] - arr[x]) {
					rs.add(Boolean.FALSE);
					flag = false;
					break;
				}
			}
			if (flag) {
				rs.add(Boolean.TRUE);
			}
		}
		return rs;
	}
	
	@Test
	public void testCheckArithmeticSubarrays() {
		int[] nums = {-12,-9,-3,-12,-6,15,20,-25,-20,-15,-10};
		int[] l = {0,1,6,4,8,7};
		int[] r = {4,4,9,7,9,10};
		System.out.println(checkArithmeticSubarrays(nums, l, r));
	}
	
}
