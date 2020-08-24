package com.yangzl;

import org.junit.Test;

/**
 * @Author: yangzl
 * @Date: 2020/4/19 22:33
 * @Desc: .. 状态机
 */
public class StateMachine {


	// =======================================================================
	// 121. 买卖股票的最佳时机
	// 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
	// 如果你最多只允许完成一笔交易（即买入和卖出一支股票一次），设计一个算法来计算你所能获取的最大利润。
	// 注意：你不能在买入股票前卖出股票。
	// 输入: [7,1,5,3,6,4] 输出: 5
	// 输入: [7,6,4,3,1] 输出: 0
	// =======================================================================
	public int maxProfit(int[] prices) {
		int n = prices.length;
		if (n < 2) return 0;
		// 状态机解法
		// dp[i][k][0] 当前第i天，总共允许k次交易，未持有股票的利润
		// dp[i][k][1]  当前第i天，总共允许k次交易，持有股票的利润

		// 该题k = 1， 所以直接降维
		int[][] dp = new int[n][2];
		// base case，第一天未持有股票利润0，持有股票利润-prices[0]
		// dp[0][0] = 0;
		dp[0][1] = -prices[0];
		for (int i = 0; i < n; ++i) {
			// 未持有股票有两种可能 -> 前一天未持有股票；前一天持有股票今天sell
			dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i]);
			// 持有股票两种可能 -> 前一天持有股票今天ret，前一天未持有股票今天buy（buy会消耗一次k，k = 0为basecase）
			dp[i][1] = Math.max(dp[i-1][1], -prices[i]);
		}
		return dp[n - 1][0];
	}

	// =======================================================================
	// 122. 买卖股票的最佳时机 II
	// 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
	// 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
	// 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
	// 输入: [7,1,5,3,6,4] 输出: 7
	// =======================================================================
	public int maxProfit2(int[] prices) {
		int n = prices.length;
		if (n < 2) return 0;
		// dp[i][k][0]
		// dp[i][k][1]
		
		// k无限直接降维，且dp[i][1]只由dp[i-1][0]这个状态转移而来（dp[i][0]同理），再降维
		int unHold = 0, hold = Integer.MIN_VALUE;
		for (int i = 0; i < n; ++i) {
			int tmp = unHold;
			// 未持有分两种情况
			unHold = Math.max(unHold, hold + prices[i]);
			// 持有分两种情况，前一天持有今天ret，前一天未持有今天buy
			hold = Math.max(hold, tmp - prices[i]);
		}
		return unHold;
	}

	// =======================================================================
	// 714. 买卖股票的最佳时机含手续费
	// 给定一个整数数组 prices，其中第 i 个元素代表了第 i 天的股票价格 ；非负整数 fee 代表了交易股票的手续费用。
	// 你可以无限次地完成交易，但是你每笔交易都需要付手续费。如果你已经购买了一个股票，在卖出它之前你就不能再继续购买股票了。
	// 返回获得利润的最大值。注意：这里的一笔交易指买入持有并卖出股票的整个过程，每笔交易你只需要为支付一次手续费。
	// 输入: prices = [1, 3, 2, 8, 4, 9], fee = 2 输出: 8
	// =======================================================================
	public int maxProfit3(int[] prices, int fee) {
		int n = prices.length;
		if (n < 2) return 0;
		int unHold = 0, hold = Integer.MIN_VALUE;
		for (int i = 0; i < n; ++i) {
			// 前一天未持有股票的利润（先保存一下）
			int tmp = unHold;
			unHold = Math.max(unHold, hold + prices[i]);
			hold = Math.max(hold, tmp - prices[i] - fee);
		}
		return unHold;
	}
	

	// =======================================================================
	// 309. 最佳买卖股票时机含冷冻期
	// 给定一个整数数组，其中第 i 个元素代表了第 i 天的股票价格 。​
	// 设计一个算法计算出最大利润。在满足以下约束条件下，你可以尽可能地完成更多的交易（多次买卖一支股票）:
	// 你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
	// 卖出股票后，你无法在第二天买入股票 (即冷冻期为 1 天)。
	// 输入: [1,2,3,0,2] 输出: 3 解释: 对应的交易状态为: [买入, 卖出, 冷冻期, 买入, 卖出]
	// =======================================================================
	public int maxProfit4(int[] prices) {
		int n = prices.length;
		if (n < 2) return 0;
		int[][] dp = new int[n][2];
		/*
		 * dp[i][k][0]
		 * dp[i][k][1]
		 * 这里k取无限，直接降维
		 * 注意含冷冻期
		 */
		// base case -> 第一天未持有股票利润位0， 持有股票利润为-prices[0]
		// dp[0][0] = 0;
		dp[0][1] = -prices[0];
		for (int i = 1; i < n; ++i) {
			dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i]);
			// 前一天未持有股票，今天买入（今天买入需要看今天之前的两天是卖出）
			// <= 0天未斥候股票的利润都是0， 所以当i <= 1时dp[i-2][0] = 0
			dp[i][1] = i > 1 ? Math.max(dp[i-1][1], dp[i-2][0] - prices[i])
					: Math.max(dp[i-1][1], 0 - prices[i]);
		}
		return dp[n-1][0];
	}
	@Test
	public void testMaxProfit4() {
		int[] prices = {1, 2, 3, 0, 2};
		System.out.println(maxProfit4(prices));
	}

	// =======================================================================
	// 188. 买卖股票的最佳时机 IV
	// 给定一个数组，它的第 i 个元素是一支给定的股票在第 i 天的价格。
	// 设计一个算法来计算你所能获取的最大利润。你最多可以完成 k 笔交易。
	// 注意: 你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
	// 输入: [2,4,1], k = 2 输出: 2
	// 解释: 在第 1 天 (股票价格 = 2) 的时候买入，在第 2 天 (股票价格 = 4) 的时候卖出，这笔交易所能获得利润 = 4-2 = 2 。
	// =======================================================================
	public int maxProfit6(int k, int[] prices) {
		int n = prices.length;
		if (n < 2 || k < 1) return 0;
		if (k > (n >>> 1)) {
			// 这里相当于k取无限，直接降维
			// 且今天持有状态只与前一天未持有状态，今天未持有只与前一天持有状态有关，所以2个状态足以保存
			int unHold = 0, hold = Integer.MIN_VALUE;
			for (int i = 0; i < n; ++i) {
				// 前一天未持有股票的利润（先保存一下）
				int tmp = unHold;
				unHold = Math.max(unHold, hold + prices[i]);
				hold = Math.max(hold, tmp - prices[i]);
			}
			return unHold;
		}
		// 状态机
		// dp[i][k][0] 当前第i天，完成k笔交易，未持有股票的最大利润
		// dp[i][k][1] 当前第i天，完成k笔交易，持有股票的最大利润
		int[][][] dp = new int[n][k + 1][2];
		// base case
		// 1 -> k = 0时， 利润为0
		// 2 -> i = 0时，第一天未持有利润为0，第一天持有股票利润为-prices[0]
		for (int i = 1; i <= k; ++i) {
			// dp[0][i][0] = 0;
			dp[0][i][1] = -prices[0];
		}
		for (int i = 1; i < n; ++i)
			for (int j = k; j >= 1; --j) {
				// 当天（i）未持有股票，前一天未持有今天ret，前一天持有今天sell
				dp[i][j][0] = Math.max(dp[i-1][j][0], dp[i-1][j][1] + prices[i]);
				// 当前持有股票，买股票需要消耗一次交易(在卖股票的时候-1同样可以)
				dp[i][j][1] = Math.max(dp[i-1][j][1], dp[i-1][j-1][0] - prices[i]);
			}
		return dp[n-1][k][0];
	}
	@Test
	public void test2() {
		int[] prices = {3, 2, 6, 5, 0, 3};
		System.out.println(maxProfit6(20, prices));
	}
}
