package com.yangzl.twopointer;

import java.util.Scanner;
import java.util.stream.Stream;

/**
 * @author yangzl
 * @date 2020/10/15 11:13
 * @desc
 */
public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String[] ND = scanner.nextLine().split(" ");
		int N = Integer.parseInt(ND[0]), D = Integer.parseInt(ND[1]);
		int[] nums = Stream.of(scanner.nextLine().split(" "))
				.limit(N).mapToInt(Integer::parseInt).toArray();

		int i = 0, j = 2;
		long rs = 0;
		while (j < nums.length) {
			while (j < nums.length - 1) {
				if (nums[j + 1] - nums[i] > D){
					break;
				}
				++j;
			}
			if (nums[j] - nums[i] <= D) {
				int count = j - i + 1;
				long tmp = factorial(count) / 6;
				rs += tmp;
			}
			++i;
			++j;
		}
		System.out.println(rs % 99997867);
	}

	static long factorial(int n) {
		long rs = 1;
		while (n > 1) {
			rs *= n;
			--n;
		}
		return rs;
	}

}
