package com.yangzl.algorithm;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangzl
 * @date 2020/2/21 14:20
 * @desc .. 回溯算法模板
 * 	rs = [];
 * 	def backtrack(路径，选择列表，条件)
 * 	    if 满足条件
 * 	    	rs.add(路径)
 * 	    	return	
 * 		for (选择 in 选择列表)
 * 			// 可提前判断是否满足条件，降低系统栈深度
 * 			if (不满足条件)
 * 				continue / break
 * 			// 当要求不能重复时，在同一层次的调用发生的重复应该避免（选择列表需排序）
 * 			if (i > start / idx && 选择列表[i] == 选择列表[i - 1])
 * 				continue
 * 			做选择
 * 			backtrack(路径，选择列表，条件变化)
 * 			撤销选择（状态回溯）
 */
public class BackTrack {

	// =======================================================================
	// 131 分割回文串
	// =======================================================================
	List<List<String>> rs = new ArrayList<>();
	public List<List<String>> partitionPalindrome(String s) {
		int n  = s.length();
		if (n == 0) return rs;
		
		partitonBT(s, 0, new ArrayDeque<>(n));
		return rs;
	}
	// 暴力回溯
	private void partitonBT(String s, int idx, Deque<String> path) {
		int n = s.length();
		if (idx == n) {
			rs.add(new ArrayList<>(path));
			return;
		}
		/*
		 * 要理解idx与i的区别
		 * idx只是记录当前函数处理到哪个位置
		 */
		for (int i = idx; i < n; ++i) {
			// 当前字符序列是回文串
			if (isPalindrome(s, idx, i)) {
				// 做选择
				path.addLast(s.substring(idx, i + 1));
				// 状态递进
				partitonBT(s, i + 1, path);
				// 状态回溯（撤销选择）
				path.removeLast();
			}
		}
	}
	// 双指针
	private boolean isPalindrome(String s, int start, int end) {
		while (start < end) 
			if (s.charAt(start++) != s.charAt(end--))
				return false;
		return true;
	}

	// =======================================================================
	// 17 电话号码字符串组合
	// =======================================================================
	final String[] letters = {"abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
	/**
	 * @Date 2020/2/18
	 * @Desc 组合9键的字母
	 */
	public List<String> letterCombinations(String digits) {
		List<String> rs = new ArrayList<>();
		if (digits.length() == 0)
			return rs;
		letterCombinationBT(digits, 0, "", rs);
		return rs;
	}
	// 暴力回溯
	private void letterCombinationBT(String digits, int idx, String str, List<String> path) {
		if (idx == digits.length()) {
			path.add(str);
			return;
		}
		String s = letters[digits.charAt(idx) - '2'];
		for (int i = 0; i < s.length(); i++)
			letterCombinationBT(digits, idx + 1, str + s.charAt(i), path);
	}

	// =======================================================================
	// 46 全排列 给定一个没有重复数字的序列，返回其所有可能的全排列。
	// =======================================================================
	List<List<Integer>> result = new ArrayList<>();
	public List<List<Integer>> permute(int[] nums) {
		boolean[] used = new boolean[nums.length];
		permuteBT(0, nums, used,  new ArrayDeque<>(nums.length));
		return result;
	}
	/*
	 * 处理到idx索引位置
	 * 区分 idx 和 具体执行时的i
	 */
	private void permuteBT(int idx, int[] nums, boolean[] used, Deque<Integer> path) {
		if (path.size() == idx) {
			result.add(new ArrayList<>(path));
			return;
		}
		// 每次都要从所有数中选择，所以判断path中是否存在该数
		for (int i = 0; i < nums.length; ++i) {
			if (!used[i]) {
				path.push(nums[i]);
				used[i] = true;
				permuteBT(idx + 1, nums, used, path);
				// 状态回溯
				used[i] = false;
				path.pop();
			}
		}
	}

	// =======================================================================
	// 77 组合 给定两个整数n和k，返回 1...n 中所有可能的k 个数的组合。即Cnk
	// =======================================================================
	public List<List<Integer>> combine(int n, int k) {
		// 复用result
		if (k < 1 || n < k) return result;
		
		combineBT(n, k, 1, new ArrayDeque<>(k));
		return result;
	}
	// 回溯。idx表示执行到第idx个元素
	private void combineBT(int n, int k, int idx, ArrayDeque<Integer> stack) {
		if (k == stack.size()) {
			result.add(new ArrayList<>(stack));
			return;
		}
		/*
		 * 从当前处理到的元素开始，idx之前的元素已经处理过
		 * n - (k - stack.size()) + 1
		 */
		for (int i = idx; i <= n; ++i) {
			stack.push(i);
			combineBT(n, k, i + 1, stack);
			stack.pop();
		}
	}

	// =======================================================================
	// 39
	// =======================================================================

	// =======================================================================
	// 40
	// =======================================================================

	// =======================================================================
	// 218
	// =======================================================================

	// =======================================================================
	// 78 子集 不包含重复元素的整数数组nums，返回该数组所有可能的子集
	// [1,2] 输出 [[],[1],[2],[1,2]]
	// =======================================================================
	/*
	 * 每个数选或不选，构成满二叉树
	 */
	public List<List<Integer>> subsets(int[] nums) {
		// 复用result
		result.add(new ArrayList<>());
		if (nums.length == 0) return result;
		subsetsBackTrace(0, nums, new ArrayList<>(nums.length));
		return result;
	}
	private void subsetsBackTrace(int idx, int[] nums, List<Integer> list) {
		if (idx >= nums.length) return;
		for (int i = idx; i < nums.length; ++i) {
			list.add(nums[i]);
			result.add(new ArrayList<>(list));
			subsetsBackTrace(i + 1, nums, list);
			// 这里回溯之后状态恰好是不选择nums[i]元素的情况，构成整颗满二叉树
			list.remove(list.size() - 1);
		}
	}

	// =======================================================================
	// 90 子集2 给定一个可能包含重复元素的整数数组nums，返回该数组所有可能的子集，子集不重复
	// [1,2,2] 输出[[2],[1],[1,2,2],[2,2],[1,2],[]]
	// =======================================================================
	public List<List<Integer>> subsetsWithDup(int[] nums) {
		rs.add(new ArrayList<>());
		if (nums.length == 0) return result;
		// 先将元素排序
		Arrays.sort(nums);
		subsetsWithDupBT(0, nums, new ArrayList<>(nums.length));
		return result;
	}
	// 每个数字选或不选，相同元素只选一次
	private void subsetsWithDupBT(int idx, int[] nums, List<Integer> list) {
		if (idx >= nums.length) return;
		for(int i = idx; i < nums.length; ++i) {
			// 回溯回来的路上，++i之后，还会再取到相同的元素， 这一次应该排除
			if (i > idx && nums[i] == nums[i - 1])
				continue;
			list.add(nums[i]);
			result.add(new ArrayList<>(list));
			subsetsWithDupBT(i + 1, nums, list);
			list.remove(list.size() - 1);
		}
	}
	
	/*
	 * =====================================================================
	 * 二维平面的回溯
	 * =====================================================================
	 */
	
	// =====================================================================
	// 12 矩阵中是否存在一条包含某字符串所有字符的路径。从矩阵中的任意一格开始，每一步可以在矩阵中向左、右、上、下移动一格。
	// 如果一条路径经过了矩阵的某一格，那么该路径不能再次进入该格子
	// =====================================================================
	public boolean exist(char[][] board, String word) {
		if (word.length() == 0) return false;
		int rows = board.length, cols = board[0].length;
		boolean[] visited = new boolean[rows * cols];
		for (int i = 0; i < rows; ++i)
			for(int j = 0; j < cols; ++j)
				if (existBackTrace(i, j, rows, cols, board, visited, word, 0))
					return true;
		return false;
	}
	// 字符串处理到idx索引处
	private boolean existBackTrace(int row, int col, int rows, int cols,
								   char[][] board, boolean[] visited, String word, int idx) {
		// 前面所有的都已匹配，返回true
		if (idx == word.length()) return true;
		int cur = row * cols + col;
		if (row >= 0 && row < rows && col >= 0 && col < cols && !visited[cur] && board[row][col] == word.charAt(idx)) {
			visited[cur] = true;
			// 在当前字符的四周搜索下一个字符是否存在
			int nextIdx = idx + 1;
			boolean flag = ( existBackTrace(row, col + 1, rows, cols, board, visited, word, nextIdx) ||
					existBackTrace(row + 1, col, rows, cols, board, visited, word, nextIdx) ||
					existBackTrace(row, col - 1, rows, cols, board, visited, word, nextIdx) ||
					existBackTrace(row - 1, col, rows, cols, board, visited, word, nextIdx) );
			// 回溯状态
			if (!flag) {
				visited[cur] = false;
			}
			return flag;
		}
		return false;
	}
	
	/*
	 *             --a
	 *           A    1
	 *          /      \
	 * 		   1		B--b
	 *        /        /   |
	 *   b-- B        2    2
	 * 	 |	 \		
	 *	 2    2		 
	 */
	

	public static void main(String[] args) {
		String regex = "服务对象<\\/td>(.*?(?:<\\/td>))";
		String str = "<td>服务对象</td><td>企业法人，非企业法人</td><td>1213123</td><td>服务对象</td><td>企业法人，非企业法人</td><td>1213123</td>";
		Matcher matcher = Pattern.compile(regex).matcher(str);
		while (matcher.find())
			System.out.println(matcher.group(1));
	}
}
