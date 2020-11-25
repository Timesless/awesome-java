package com.yangzl;

import org.junit.jupiter.api.Test;

/**
 * @author yangzl
 * @date 2020/4/19 08:27
 * @desc ..
 */
public class DP {
	
	/*
	 * 0-1背包，背包容量W，物品N个，第i个物品重量为w[i]，价值为v[i]
	 */
	
	/*
	 * 字符串编辑距离
	 * 	-------------------
	 * 	|替换/跳过| 删除   |
	 * 	-------------------
	 * 	|插入    |dp[i][j]|
	 * 	-------------------
	 * 	从s1 -> s2 （或者从s2 -> s1是相同的情况）
	 * 		s1插入一个字符与s2相同， dp[i][j] = dp[i][j-1]，当前位置i插入一个字符，s2还在上一个索引即j-1
	 */


	// ###################  这个题数组的遍历方向和结果和其它不太相同，重点
	// =======================================================================
	// 516 最长回文子序列
	// 给定一个字符串s，找到其中最长的回文子序列。可以假设s的最大长度为1000。
	// "bbbab" 输出:4 一个可能的最长回文子序列为 "bbbb"。
	// =======================================================================
	public int longestPalindromeSubseq(String s) {
		int len = s.length();
		if (len < 2) return len;
		// 状态：令dp[i][j]为字符串子数组[i...j]，最长回文子序列的长度
		// 状态转移： 如果s[i-1] = s[j+1] -> dp[i-1][j+1] = dp[i][j] + 2;
		// 如果s[i-1] != s[j + 1] -> dp[i-1][j+1] = max(dp[i-1][j], dp[i][j+1]);
		// 初始化，当i = j即只有一个字符时，dp[i][i] = 1;
		// 我们需要的最终结果在 dp[0][len-1]，或者在dp[len-1][0]，这里我们选择dp[0][len - 1];
		int[][] dp = new int[len][len];
		for (int i = 0; i < len; ++i)
			dp[i][i] = 1;
		// 从下到上，从左到右遍历
		for (int i = len - 2; i >= 0; --i)
			for (int j = i + 1; j < len; ++j) {
				if (s.charAt(i) == s.charAt(j))
					// 这里要明确：递进方向是i在减少j在增加，所以上一步应该是i+1，j-1
					dp[i][j] = dp[i+1][j-1] + 2;
				else
					dp[i][j] = Math.max(dp[i+1][j], dp[i][j-1]);
			}
		return dp[0][len-1];
	}
	
	/*
	 * 博弈游戏dp通用模板
	 */
	public boolean stoneGame(int[] piles) {
		/*
		 * 状态：令dp[i][j][0]表示先手玩家，在选择石头子堆为[i...j]时获取的最高分数
		 * 		令dp[i][j][1]表示后手玩家，在选择石头子堆为[i...j]时获取的最高分数
		 * 	栗子 dp[0][N-1][0] 表示先手最后得分
		 * 		 dp[0][N-1][0] 表示后手最后得分
		 * 
		 * 初始化，当i = j时，表明只有一堆可选择
		 * 		dp[i][i][0] = piles[0]
		 * 		dp[i][i][1] = 0;
		 * 
		 * 状态转移方程：
		 * 	先手 -> 取走左边一堆（i-1），取走右边一堆（j + 1）
		 * 	dp[i][j][0] = max(选择左边石头堆, 选择右边石头堆)
		 * 	即：dp[i][j][0] = max(piles[i] + dp[i+1][j][1], piles[j] + dp[i][j-1][1]);
		 * 		此时，先手选了，轮到对方，所以我变为后手，后手变为先手
		 * 
		 * 	先手的选择会影响后手，那么
		 * 		if (先手选择左边）
		 * 			dp[i][j][1] = dp[i+1][j][0]
		 * 		id （现手选择右边）
		 * 			dp[i][j][1] = dp[i][j-1][0]
		 * 	dp结果在dp[0][N-1]地方， 所以我们可以选择斜着中间到右上遍历 或者 从下到上从左到右 遍历数组累积dp
		 */
		int N = piles.length;
		int[][][] dp = new int[N][N][2];
		// initial base case
		for (int i = 0; i < N; ++i) {
			// 只有一堆石头，先手得分，后手为0
			dp[i][i][0] = piles[i];
			// dp[i][i][1] = 0;
		}
		for (int i = N - 2; i >= 0; --i)
			for (int j = i + 1; j < N; ++j) {
				// 先手 -> 左边 | 右边
				int left = piles[i] + dp[i+1][j][1], right = piles[j] + dp[i][j-1][1];
				dp[i][j][0] = Math.max(left, right);
				if (left > right)
					dp[i][j][1] = dp[i+1][j][0];
				else 
					dp[i][j][1] = dp[i][j-1][0];
			}
		return dp[0][N-1][0] > dp[0][N-1][1];
	}

	/*
	 * 递归解法
	 */
	public boolean isMatch(String s, String p) {
		int sln = s.length(), pln = p.length();
		if (sln == 0 && pln == 0) return true;
		if (sln != 0 && pln == 0) return false;
		boolean firstMatch = !s.isEmpty()
				&& (s.charAt(0) == p.charAt(0) || p.charAt(0) == '.');
		// *号，匹配0个或者多个前面那一个元素
		if (pln > 1 && p.charAt(1) == '*') {
			return (firstMatch && isMatch(s.substring(1), p))
					|| isMatch(s, p.substring(2));
		}
		return firstMatch && isMatch(s.substring(1), p.substring(1));
	}
	@Test
	public void testIsMatch() {
		System.out.println(isMatch("aaa", "a*"));
	}
	
	
	/*
	 * hard 887.鸡蛋掉落
	 * 状态 -> 令 鸡蛋数为K，楼层为N时，存在楼层F使[0...F]之间鸡蛋都不会被摔碎
	 * 状态转移 ->
	 * 		1. 当前鸡蛋被摔碎，dp[K-1][i-1]（i-1表示楼层[1...i-1]）
	 * 		2. 当前鸡蛋没碎， dp[K][N-i]
	 * 	base case:
	 * 		当楼层为0，不需要扔鸡蛋
	 * 		当鸡蛋为1，只能线性扫描所有楼层
	 */
	public int superEggDrop(int K, int N) {
		if (N == 1) return 1;
		if (K == 1) return N;
		int[][] dp = new int[K + 1][N + 1];
		// base case 1: 楼层为1时， 只需要扔一次可以确定F
		for (int i = 1; i <= K; ++i)
			dp[i][1] = 1;
		// base case 2: 鸡蛋为1时，每个楼层都需要测试才能知道具体F
		for (int i = 1; i <= N; ++i)
			dp[1][i] = i;
		
		for (int i = 1; i <= K; ++i) {
			for (int j = 1; j <= N; ++j) {
				int min = N * N;
				for (int x = 1; x <= j; ++x)
					min = Math.min(min, Math.max(dp[i-1][x-1], dp[i][j-x]) + 1);
				dp[i][j] = min;
			}
			
		}
		return dp[K][N];
	}
	@Test
	public void testSuperEggDrop() { System.out.println(superEggDrop(3, 14)); }

	// =======================================================================
	// 
	// =======================================================================


	// =======================================================================
	// 
	// =======================================================================
	
}
