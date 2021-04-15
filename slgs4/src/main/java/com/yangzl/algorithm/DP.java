package com.yangzl.algorithm;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author yangzl
 * @date 2020/2/19 20:46
 * @desc .. 动态规划： 求一个问题的最优解（通常是最大最小值）
 * 
 * 	1. 重叠子问题
 * 		原问题可分解为若干子问题，且子问题之间有重叠的更小的子问题
 * 		子问题在分解大问题的过程中重复出现，为避免重复计算，我们可以自底向上计算小问题的最优解并存储（一维 | 二维数组）
 * 	2. 最优子结构
 * 		原问题最优解依赖于各个子问题的最优解
 * 	
 * 	状态
 * 		函数的定义
 * 	状态转移方程
 * 		函数怎么做? （递归解法向基准情形递进时就相当于在状态转移）
 * 	
 * 	+ 定义子问题最优解的保存时，可能会定义memo[n+1]，此时memo[i]==状态转移过程中的i-1
 * 	
 * 	+ 定义基准值 | 边界值
 * 		定义基准值会减少（双重）循环
 * 		定义边界值不准时，可能需要在双重循环（具体状态转移过程中）中判断边界
 * 	
 * 	0-1背包
 * 		令f(n, c) = 将n个物品装入容量为c的背包，获得的最大价值
 * 		则状态转移方程：
 * 			f(i, c) = max{ f(i-1, c), v[i] + f(i-1, c-w[i]) }	
 * 			
 * 	完全背包（一般用于计算组合总数，所以不必max）
 * 		令dp[i][j] = 前i个物品放入容量j背包的组合的不同种数
 * 		当容量足够： j >= w[i] -> dp[i][j] = dp[i][j-w[j]] + dp[i-1][j]
 * 		否则 dp[i][j] = dp[i-1][j]	
 * 	
 * 	动态规划：每一步可能面临若干选择，事先不知道最优解，只好遍历所有可能并比较，得出最优解
 * 	贪心算法：每一个做出一个贪婪选择，基于该选择我们确定能得到最优解（需要数学证明贪心选择的正确性）
 */
public class DP {
	
	// =======================================================================
	// 343 给定一个正整数 n，将其拆分为至少两个正整数的和，使这些整数的乘积最大化并返回
	// =======================================================================
	
	/*
	 * TODO 基于贪心算法，尽可能的分解为3，如果最后有4，那么分解2 * 2
	 * 动态规划
	 */
	public int integerBreak(int n) {
		if (n < 2) return 0;
		if (n == 2) return 1;
		if (n == 3) return 2;
		
		// 保存子问题最优解
		int[] memo = new int[n + 1];
		// 0 1不拆
		memo[2] = 1;
		memo[3] = 2;
		for (int i = 4; i <= n; ++i) {
			int max = -1;
			// 遍历所有可能，选出最大值并存储
			for (int j = 1; j <= i / 2; ++j) {
				int product = memo[j] * memo[i - j];
				if (max < product) 
					max = product;
				memo[n] = max;
			}
			return memo[n];
		}
		return 0;
	}

	// =======================================================================
	// 300 最长上升子序列
	// TODO 找出所有LIS
	// =======================================================================
	/*
	 * LIS(i)表示以第i个数字为结尾的最长上升子序列的长度
	 * 对于当前数，我们可以选择，可以不选择
	 */
	public int lengthOfLIS(int[] nums) {
		int n = nums.length;
		int[] memo = new int[n];
		// 每个数字LIS初始为1
		for (int i = 0; i < n; i++)
			memo[i] = 1;
		
		for (int i = 1; i < n; i++) 
			// 当前数的前面任意一个数字小于当前数，LIS++
			for (int j = 0; j < i; ++j) 
				if (nums[i] > nums[j]) 
					// 判断
					memo[i] = Math.max(memo[i], 1 + memo[j]);
		return Arrays.stream(memo).max().getAsInt();
	}
	@Test
	public void testlengthOfLIS() {
		int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
		System.out.println(lengthOfLIS(nums));
	}

	// =======================================================================
	// 1143 最长公共子序列longestCommonSubsequence
	// =======================================================================
	public int LCS(String text1, String text2) {
		int m = text1.length(), n = text2.length();
		if (m == 0 || n == 0)
			return 0;
		int[][] memo = new int[m][n];
		return LCSMemo(m - 1, text1, n - 1, text2, memo);
	}
	// text1的第i个位置，text2的第j个位置
	private int LCSMemo(int i, String text1, int j, String text2, int[][] memo) {
		if (i < 0 || j < 0)
			return 0;
		if (memo[i][j] != 0)
			return memo[i][j];
		char t1 = text1.charAt(i);
		char t2 = text2.charAt(j);
		int max = -1;
		if (t1 == t2)
			max = 1 + LCSMemo(i - 1, text1, j - 1, text2, memo);
		else 
			max = Math.max(LCSMemo(i - 1, text1, j, text2, memo),
					LCSMemo(i, text1, j - 1, text2, memo));
		memo[i][j] = max;
		return max;
	}
	/*
	 * 状态： memo[i][j] 表示text1在第i个位置，text2在第j个位置的LCS值
	 * 状态转移方程
	 * charAt(i) == charAt(j) ? 1 + memo[i-1][j-1] : max {memo[i-1][j], memo[i], [j-1]}
	 */
	public int LCSDP(String text1, String text2) {
		int m = text1.length(), n = text2.length();
		if (m == 0 || n == 0)
			return 0;
		int[][] memo = new int[m + 1][n + 1];
		for (int i = 1; i <= m; ++i)
			for (int j = 1; j <= n; ++j) {
				/*
				 * 这里注意memo[i][j]与charAt(i -1),charAt(j - 1)是等价的
				 * 即memo[i][j]对应text1,text2的i-1，j-1
				 */
				char t1 = text1.charAt(i - 1);
				char t2 = text2.charAt(j - 1);
				if (t1 == t2)
					memo[i][j] = 1 + memo[i - 1][j - 1];
				else
					memo[i][j] = Math.max(memo[i - 1][j], memo[i][j - 1]);
			}
		return memo[m][n];
	}

	// =======================================================================
	// 376 最长摆动序列
	// =======================================================================
	

	// =======================================================================
	// 0-1背包问题
	// =======================================================================
	// 物品重量w[]，物品价值v，背包容量c
	public int backpack0_1(int[] w, int[] v, int c) {
		int[][] memo = new int[w.length][c + 1];
		// 0...w.length - 1个物品装入容量为c背包的最大价值
		return backpack0_1Memo(w.length - 1, memo, w, v, c);
	}
	private int backpack0_1Memo(int idx, int[][] memo, int[] w, int[] v, int c) {
		if (idx < 0 || c < 1)
			return 0;
		if (memo[idx][c] != 0)
			return memo[idx][c];
		// f(i,c) = 装或不装第i个物品的最大值
		int unload = backpack0_1Memo(idx - 1, memo, w, v, c);
		int load = -1;
		if (c >= w[idx])
			load = v[idx] + backpack0_1Memo(idx - 1, memo, w, v, c - w[idx]);
		memo[idx][c] = Math.max(unload, load);
		return memo[idx][c];
	}
	/*
	 * 0-1背包动态规划
	 * memo[2][c + 1]保存子问题的最优解（行号模2，空间复杂度Oc）
	 */
	public int backpack0_1DP(int[] w, int[] v, int c) {
		int n = w.length;
		int[][] memo = new int[n + 1][c + 1];
		/*
		 * 第1个物品在容量为j的背包的最大价值
		 * 初始化值，这样可少依次双重for循环
		 */
		for (int j = 0; j <= c; ++j)
			memo[1][j] = j >= w[1] ? v[1] : 0;
		/*
		 * 求出第一个物品，在容量为j的背包的最大价值
		 * 求出第二个物品，在容量为j的背包的最大价值
		 * 求出第i 个物品...
		 * 第二个物品容量至少为2，所以j从2开始
		 */
		for (int i = 2; i <= n; ++i) {
			// 容量为j下，第i个物品的价值
			for (int j = 2; j <= c; ++j) {
				// wv相关索引，要和memo索引区分开
				int wv = i - 1;
				int unload = memo[i - 1][j];
				int load = -1;
				if (j >= w[wv])
					load = v[wv] + memo[i - 1][j - w[wv]];
				memo[i][j] = Math.max(unload, load);
			}
		}
		return memo[n][c];
	}
	@Test
	public void testBackpack0_1DP() {
		int[] w = {1, 2, 3};
		int[] v = {6, 10, 12};
		System.out.println(backpack0_1DP(w, v, 5));
		System.out.println(backpack0_1(w, v, 5));
	}
	
	// =======================================================================
	// 198 相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
	// 在不触动警报装置的情况下，能够偷窃到的最高金额。
	// =======================================================================
	/**
	 * @Date: 2020/2/19 记忆化搜索
	 * @Desc: 
	 **/
	public int rob(int[] nums) {
		int n = nums.length;
		// rangeCheck
		if (n < 1) return 0;
		if (n == 1) return nums[0];
		if (n == 2) return Math.max(nums[0], nums[1]);
		
		int[] memo = new int[n];
		return robMemo(nums, 0, memo);
	}
	// 从i位置，偷取nums[idx...size)范围内的房子
	private int robMemo(int[] nums, int idx, int[] memo) {
		if (idx >= nums.length) return 0;
		if (memo[idx] != 0) return memo[idx];
		// 遍历所有可能，获取最大值并保存
		int max = -1;
		for (int i = idx; i < nums.length; ++i) {
			int cur = nums[i] + robMemo(nums, i + 2, memo);
			if (max < cur)
				max = cur;
		}
		memo[idx] = max;
		return max;
	}
	/**
	 * @Date: 2020/2/19 动态规划
	 * @Desc: 
	 **/
	public int robDP_1(int[] nums) {
		// rangeCheck(); 同上
		int n = nums.length;
		int[] memo = new int[n];
		for (int i = 0; i < n; ++i)
			memo[i] = -1;
		
		// 定义边界值 | 基准值，当定义不准确时，需要在循环中判断，如下 j+3 > n
		memo[n - 1] = nums[n - 1];
		for (int i = n - 2; i >= 0; --i) {
			int max = -1;
			// 遍历所有可能，获取最大值并存储
			for (int j = i; j < n; ++j) {
				int cur = (j + 3) > n ? nums[j] : nums[j] + memo[j + 2];
				if (max < cur)
					max = cur;
			}
			memo[i] = max;
		}
		return memo[0];
	}
	/**
	 * @Date: 2020/2/19 改变对状态的定义
	 * @Desc: 
	 **/
	public int robDP_2(int[] nums) {
		// rangeCheck();
		int n = nums.length;
		int[] memo = new int[n];
		for (int i = 0; i < n; ++i)
			memo[i] = -1;
		/*
		 * 2020年2月29日，其实这里替换为memo[1] = nums[0]可将时间复杂度降为On
		 */
		memo[0] = nums[0];
		// max[2] = max{ nums[2], nums[0] + num[2] }
		for (int i = 1; i < n; ++i ) {
			int max = -1;
			for (int j = i ; j >= 0; --j) {
				// 边界判断
				int cur = j - 2 >= 0 ? nums[j] + memo[j - 2] : nums[j];
				if (max < cur)
					max = cur;
			}
			memo[i] = max;
		}
		return memo[n - 1];
	}
	/*
	 * 改变对初始值的定义
	 */
	public int robDP_3(int[] nums) {
		// rangeCheck();
		/*
		 * 状态：令dp[n]表示[0...n]可以偷取的最大价值
		 * 状态转移：偷取 | 不偷取
		 * 	转移方程: dp[i] = max {dp[i - 1], dp[i - 2] + nums[（i对应索引为i - 1）]}
		 */
		int[] dp = new int[nums.length + 1];
		// 初始化值，只有一间时偷取
		dp[1] = nums[0];
		for (int i = 2; i <= nums.length; ++i)
			// 注意这里 dp[i] & nums[i - 1]对应
			dp[i] = Math.max(dp[i - 2] + nums[i - 1], dp[i - 1]);
		return dp[nums.length];
	}
	
	// =======================================================================
	// 62 机器人位于m x n 网格的左上角，每次只能向下或者向右移动一步。机器人试图达到网格的右下角
	// 问总共有多少条不同的路径？
	// =======================================================================
	public int uniquePathsDP(int m, int n) {
		/*
		 * 状态： 令dp[i][j]表示机器人到达i,j位置时不同路径的总数
		 * 状态转移：
		 * 	从左边即dp[i][j - 1]处向右走一步到达dp[i][j]
		 * 	从上边即dp[i - 1][j]处向下走一步到达dp[i][j]
		 * 	转移方程：dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
		 * n行m列，其实对调mn，m表示行n表示列
		 */
		int[][] dp = new int[n][m];
		// 初始条件，当在最左列和最上行只有一种不同路径（向右 | 向下）
		for (int i = 0; i < m; ++i)
			dp[0][i] = 1;
		for (int i = 0; i < n; ++i)
			dp[i][0] = 1;
		for (int i = 1; i < n; ++i)
			for (int j = 1; j < m; ++j)
				dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
		return dp[m - 1][n - 1];
	}

	// =======================================================================
	// 64 一个包含非负整数的m x n网格，请找出一条从左上到右下的路径使得路径上的数字总和为最小。
	// =======================================================================
	public int minPathSumDP(int[][] grid) {
		int m = grid.length;
		int n = grid[0].length;
		int[][] dp = new int[m][n];
		// 初始化值
		dp[0][0] = grid[0][0];
		for (int i = 1; i < m; ++i)
			dp[i][0] = dp[i - 1][0] + grid[i][0]; 
		for (int i = 1; i < n; ++i)
			dp[0][i] = dp[0][i - 1] + grid[0][i];
		for (int i = 1; i < m; ++i)
			for (int j = 1; j < n; ++j)
				dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
		
		return dp[m - 1][n - 1];
	}

	// =======================================================================
	// 72 给定两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数 
	//你可以对一个单词进行如下三种操作：插入一个字符，删除一个字符，替换一个字符
	// =======================================================================
	public int minDistanceDP(String word1, String word2) {
		/*
		 * 状态：令dp[i][j]表示word1长度为i时转换为word2长度为j时的最少操作数
		 * 状态转移：
		 * 	word1处理索引为i，word2处理索引为j
		 * 	当：word1[i] == word[j]那么不用操作，dp[i][j] = dp[i - 1][j - 1]
		 * 	否则：word1[i]添加一个字符之后与word2[j]相同,dp[i][j] = dp[i][j - 1] + 1
		 * 			添加之后其实就是 word1[i] == word2[j - 1]
		 * 		word1[i]删除一个字符之后与word2[j]相同,dp[i][j] = dp[i - 1][j] + 1
		 * 			删除之后就是word1[i - 1] == word2[j]
		 * 		word1[i]替换一个字符之后与word2[j]相同,dp[i][j] = dp[i - 1][j - 1] + 1
		 * 			替换之后就是word1[i - 1][j - 1]
		 * 
		 * 	转移方程：dp[i][j] = min { dp[i-1][j], dp[i - 1][j - 1], dp[i][j - 1] } + 1
 		 */
		int d = word1.length(), p = word2.length();
		if (d * p == 0)
			return d + p;
		// 考虑是否该取 + 1
		int[][] dp = new int[d + 1][p + 1];
		// 初始化值
		// word2长度为0
		for (int i = 0; i <= d; ++i)
			dp[i][0] = i;
		// word1长度0
		for (int i = 0; i <= p; ++i)
			dp[0][i] = i;
		// word1[i - 1] & dp[i]之间对应
		for (int i = 1; i <= d; ++i)
			for (int j = 1; j <= p; ++j) {
				if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					dp[i][j] = minVal(dp[i - 1][j - 1], dp[i - 1][j], dp[i][j - 1]) + 1;
				}
			}
		return dp[d][p];
	}
	private int minVal(int r, int d, int a) {
		return Math.min(Math.min(r, d), a);
	}
	
	// =======================================================================
	// 72 输入一个整型数组，数组里有正数也有负数。数组中的一个或连续多个整数组成一个子数组
	// 求所有子数组的和的最大值。要求时间复杂度为O(n)
	// =======================================================================
	public int maxSubArrayDP(int[] nums) {
		/*
		 * 状态：令dp[n]表示当索引长度为n时的子数组的最大值
		 * 状态转移：选当前数 | 选当前数 + 以前的值
		 * 	转移方程
		 * 		dp[n] = max {dp[n - 1] + nums[n], nums[n]}
		 */
		int[] dp = new int[nums.length];
		int max = dp[0] = nums[0];
		for (int i = 1; i < nums.length; ++i) {
			dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
			if (max < dp[i])
				max = dp[i];
		}
		return max;
	}
	// dp的优化 => 空间复杂度O1
	public int maxSubArray(int[] nums) {
		int max, curMax;
		max = curMax = nums[0];
		for (int i = 1; i < nums.length; ++i) {
			// 如果前面的数 < 0 直接丢弃
			if (curMax < 0)
				curMax = nums[i];
			else
				curMax += nums[i];
			if (max < curMax)
				max = curMax;
		}
		return max;
	}

	// =======================================================================
	// 72 按摩师会收到源源不断的预约请求，每个预约都可以选择接或不接。在每次预约服务之间要有休息时间，
	// 因此她不能接受相邻的预约。给定一个预约请求序列，替按摩师找到最优的预约集合（总预约时间最长），返回总的分钟数。
	// =======================================================================
	public int massageDP(int[] nums) {
		/*
		 * 状态：令dp[n]表示在索引长度为n[0...n]时，最优预约结合的总分钟数
		 * 状态转移
		 */
		if (nums.length == 0) return 0;
		if (nums.length == 1) return nums[0];
		if (nums.length == 2) return Math.max(nums[0], nums[1]);
		int[] dp = new int[nums.length + 1];
		// 初始值，只有一个预约时
		dp[1] = nums[0];
		for (int i = 2; i <= nums.length; ++i) {
			dp[i] = Math.max(dp[i - 2] + nums[i - 1], dp[i - 1]);
		}
		return dp[nums.length];
	}

	// =======================================================================
	// 72 给定字符串 s 和 t ，判断 s 是否为 t 的子序列。
	// 你可以认为 s 和 t 中仅包含英文小写字母。字符串 t 可能会很长（长度 ~= 500,000），而 s 是个短字符串（长度 <=100）。
	// 子序列是原始字符串删除 |不删除字符而不改变剩余字符相对位置形成的新字符串。（"ace"是"abcde"的一个子序列，而"aec"不是）。
	// =======================================================================
	public boolean isSubsequenceDP(String s, String t) {
		/*
		 * 状态：令dp[i][j]表示s[i]（s处理到长度为i）是否是t[j]（t处理到长度为j位置）的子序列
		 * 状态转移：
		 * 	s[i] == t[j]时，dp[i][j] = dp[i-1][j-1]
		 * 	否则：删除一些字符dp[i][j] = dp[i][j-1]
		 */
		int d = s.length(), p = t.length();
		if (d < 1) return true;
		if (p < 1 || d > p) return false;
		/*
		 * 没判断s[0] & t[0]是否相同，所以应该取d + 1, p + 1
		 * 那么dp[i][j]分别对应s[i - 1],t[j - 1]
		 */
		boolean[][] dp = new boolean[d + 1][p + 1];
		// 初始化值，s为空，t不空时true
		for (int i = 0; i < p; ++i)
			dp[0][i] = true;
		
		for (int i = 1; i <= d; ++i)
			for (int j = 1; j <= p; ++j) {
				if (s.charAt(i - 1) == t.charAt(j - 1))
					dp[i][j] = dp[i - 1][j - 1];
				else {
					// 字符不相同，那么可删除t某些字符
					dp[i][j] = dp[i][j - 1];
				}
			}
		return dp[d][p];
	}

	// =======================================================================
	// 给定数量不限的硬币，币值为25分、10分、5分和1分，编写代码计算n分有几种表示法。结果模上1000000007
	// =======================================================================
	public int waysToChangeDP(int n) {
		if (n < 5) return 1;
		if (n == 5) return 2;
		int[] coins = {1, 5, 10, 25};
		int[][] dp = new int[4][n + 1];
		// 当数量为0，1时，分别有1种表示法
		for(int i = 0; i < 4; ++i){
			dp[i][0] = 1;
			dp[i][1] = 1;
		}
		// 当只有一种硬币时，只有1种表示法
		for(int i = 0; i <=n; ++i)
			dp[0][i] = 1;
		/*
		 * 状态：dp[i][j]表示[0...i]种硬币能组合为j的所有不同种数
		 * 状态转移：取 或 不取
		 */
		for (int i = 1; i < 4; ++i) 
			for (int j = 2; j <= n; ++j) {
				if (j >= coins[i])
					dp[i][j] = (dp[i][j - coins[i]] + dp[i - 1][j]) % 1000000007;
				else
					dp[i][j] = dp[i - 1][j];
			}
		return dp[3][n];
	}
	@Test
	public void testWays2ChangeDP() {
		System.out.println(waysToChangeDP(62));
	}
	
	// =======================================================================
	// 在一个 m*n 的棋盘的每一格都放有一个礼物，每个礼物都有一定的价值（价值大于 0）。你可以从棋盘的左上角开始拿格子里的礼物，
	// 并每次向右或者向下移动一格、直到到达棋盘的右下角。给定一个棋盘及其上面的礼物的价值，请计算你最多能拿到多少价值的礼物？
	// =======================================================================
	public int maxValueDP(int[][] grid) {
		int m = grid.length, n = grid[0].length;
		int[][] dp = new int[m + 1][n + 1];
		// 初始化值
		for (int i = 0; i < m; ++i)
			dp[i][0] = 0;
		for(int i = 0; i < n; ++i)
			dp[0][i] = 0;
		/*
		 * 状态转移：从左边到grid[i][j]，从上边到grid[i][j]，取两者的最大值
		 * 转移方程：max { grid[i-1][j], grid[i][j-1] } + grid[i - 1][j -1]
		 *  注意这里dp[i][j] 对应 grid[i-1][j-1]
		 */
		int di, dj;
		for (int i = 1; i <= m; ++i)
			for (int j = 1; j <= n; ++j) {
				di = i - 1;
				dj = j - 1;
				dp[i][j] = Math.max(dp[i][dj], dp[di][j]) + grid[di][dj];
			}
		return dp[m][n];
	}
	
	// =======================================================================
	// 338 比特位计数 给定一个非负整数 num。对于 0 ≤ i ≤ num 范围中的每个数字i计算其二进制数中的 1 的数目并将它们作为数组返回。
	// 输入: 2 输出: [0,1,1]；输入: 5 输出: [0,1,1,2,1,2]
	// =======================================================================
	public int[] countBitsDP(int num) {
		int[] dp = new int[num + 1];
		// 状态转移： 对于奇数，是前一个数比特为1的个数 + 1，对于偶数 = i >>> 1的比特位1的个数
		// 101 = 100 + 1; 1000 = 100
		for (int i = 1; i <= num; ++i) {
			if ((i & 1) == 0) 
				dp[i] = dp[i >>> 1];
			else
				dp[i] = dp[i - 1] + 1;
		}
		return dp;
	}

}
