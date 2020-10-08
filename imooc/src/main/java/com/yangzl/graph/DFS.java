package com.yangzl.graph;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangzl
 * @date 2020/10/7 18:43
 * @desc 图的深度优先遍历
 * 
 * 	时间复杂度：O(V + E)
 * 	作用：
 * 		图是否连通，有几个连通分量
 * 		两点间是否可达
 * 		二分图的检测	
 * 		
 * 	真正的理解遍历，就可以解决80%的面试问题，<em>很多算法本质都是“遍历”</em>
 * 	
 * 	需要记录节点是否被遍历了， 因为图是有环的
 * 	
 * 	这复习一下树的遍历
 * 		请掌握二叉树的前中后序遍历、且完成递归与迭代的写法
 * 		掌握树的层序遍历、前后序遍历的递归写法。 多叉树是没有中序遍历的	
 */
public class DFS {
	
	public static final String GRAPH = "graph.txt";
	public static final String MULTI = "multigraph.txt";

	Graph graph;
	boolean[] visited;
	List<List<Integer>> rs;
	
	/* 
	 * 深度优先遍历模板，请认真理解
	 * dfs(int v) {
	 * 		visited[v] = true;
	 * 		list.add(v);
	 * 		for (int w : adj(v)) {
	 * 			if (!visited[w])
	 * 				dfs(w);
	 * 		}
	 * }
	 */
	// 建议迭代实现先序遍历
	public void dfs(int vertexNum) {
		/*
		 * 对于多个连通分量，我们需要通过for去遍历
		 * 添加count变量即可获取连通分量的个数
		 */
		for (int v = 0; v < vertexNum; ++v) {
			if (!visited[v]) {
				List<Integer> list = new ArrayList<>(graph.degree(v));
				preDFS(list, v);
				rs.add(list);
			}
		}
	}
	// 深度优先遍历的先序遍历
	private void preDFS(List<Integer> list, int v) {
		list.add(v);
		visited[v] = true;
		for (int w : graph.adj(v)) {
			if (!visited[w])
				preDFS(list, w);
		}
	}
	// 深度优先遍历的后序遍历，当有向图时会有用
	private void postDFS(List<Integer> list, int v) {
		visited[v] = true;
		for (int w : graph.adj(v)) {
			if (!visited[w])
				postDFS(list, w);
		}
		list.add(v);
	}


	// ================================================================
	// 图的遍历的应用
	// ================================================================
	
	/*
	 * 图是否连通，有几个连通分量
	 * 两点间是否可达
	 * 二分图的检测
	 * 寻找图中的桥
	 * 寻找图中的割点
	 * 哈密尔顿路径
	 * 拓扑排序
	 *
	 * 非递归实现深度优先遍历的先序遍历
	 * 邻接矩阵的深度优先遍历
	 */


	/**
	 * 单源路径从 0 是否能到 6，0 - 6的路径是什么
	 * 暂时不考虑最短路径
	 * @param v1 源顶点
	 * @param v2 目标顶点
	 */
	public List<Integer> singleSourcePath(int v1, int v2) {
		// 根据v1深度优先遍历，可以提前终止
		List<Integer> list = new ArrayList<>(graph.V());
		preDFSWithBreak(list, v1, v2);
		if (!visited[v2]) {
			return Collections.emptyList();
		}
		// int idx = list.indexOf(v2);
		// return idx > 0 ? list.subList(0, idx + 1) : Collections.emptyList();
		return list;
	}
	private boolean preDFSWithBreak(List<Integer> list, int v1, int v2) {
		System.out.println(v1);
		list.add(v1);
		visited[v1] = true;
		if (v1 == v2) {
			return true;
		}
		for (int w : graph.adj(v1)) {
			if (!visited[w]) {
				/*
				 *             0
				 *         /      \
				 *      1  ——  3 —— 2
				 *       \			\
				 * 		  4		5	 6
				 * 假设求0 -> 3路径
				 * 上面的 v1 == v2只能在3的地方终止，逻辑依然会在for循环4的地方继续
				 * 如果4连接了很多顶点，那么程序是停不下来的， 但如果在for中判断
				 * 就不会执行1连接的其它节点，比如4. 逻辑整体会在 3的地方终止，返回到1，再返回到0
				 * 这里0的相邻节点2都不会执行了，因为在for中判断的
				 */
				if (preDFSWithBreak(list, w, v2))
					return true;
			}
		}
		return false;
	}
	
	/*
	 * 注意比较与上面程序的区别
	 * 请深刻理解
	 */
	public void singleSourcePathNoReturn(int v1, int v2) {
		System.out.println(v1);
		visited[v1] = true;
		// 这里并不能终止逻辑
		if (v1 == v2) {
			return;
		}
		for (int w : graph.adj(v1)) {
			if (!visited[w]) {
				/*
				 *             0
				 *         /      \
				 *      1  ——  3 —— 2
				 *       \			\
				 * 		  4		5	 6
				 * 假设求0 -> 3路径
				 * 上面的 v1 == v2只能在3的地方终止，逻辑依然会在for循环4的地方继续
				 * 如果4连接了很多顶点，那么程序是停不下来的， 但如果在for中判断
				 * 就不会执行1连接的其它节点，比如4. 逻辑整体会在 3的地方终止，返回到1，再返回到0
				 * 这里0的相邻节点2都不会执行了，因为在for中判断的
				 */
				singleSourcePathNoReturn(w, v2);
			}
		}
	}
	
	
	/**
	 * 2020/10/8 无向图的环检测
	 *  判断当前节点的邻接节点是否在list中，即可知道是否有环
	 * 
	 * @param vertexNum 顶点的个数
	 * @return void
	 */
	public void cycleDetected(int vertexNum) {
		for (int v = 0; v < vertexNum; ++v) {
			if (!visited[v]) {
				System.out.println(v);
				List<Integer> list = new ArrayList<>(vertexNum);
				// 如果只需要一个环，这里可以break
				if (loopDetected(list, v, v)) {
					rs.add(list);
					// break;
				}
			}
		}
		rs = rs.stream().filter(list -> list.size() > 1).collect(Collectors.toList());
	}
	/**
	 *             0
	 *         /      \
	 *      1  ——  3 —— 2
	 *       \			\
	 * 		  4		5	 6
	 * @param list 记录环
	 * @param v 当前节点
	 * @param parent 当前节点的前一个节点   
	 */
	private boolean loopDetected(List<Integer> list, int v, int parent) {
		visited[v] = true;
		list.add(v);
		
		for (int w : graph.adj(v)) {
			if (!visited[w]) {
				if (loopDetected(list, w, v))
					return true;
			// 这里当4 作为第一个节点传入时， 也能成功，所以结果判断一下list.size呗
			// 无奈之举
			} else if (w != parent) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 2020/10/8 二分图的检测，染色（相邻顶点染成不同颜色）
	 * 	0	
	 * 	3	1
	 * 	4	2
	 * 		5
	 * 	6
	 * @param vertexNum
	 * @return boolean
	 */
	public boolean binaryGraph(int vertexNum) {
		for (int v = 0; v < vertexNum; ++v) {
			int[] colors = new int[vertexNum];
			Arrays.fill(colors, -1);
			if (!dfsColor(v, colors, 0)) {
				return false;
			}
		}
		return true;
	}
	private boolean dfsColor(int v, int[] colors, int color) {
		colors[v] = color;
		for (int w : graph.adj(v)) {
			// 没有被访问才进入逻辑
			if (colors[v] == -1) {
				if (!dfsColor(w, colors, 1 - color))
					return false;
			// 两个相邻节点颜色相同就不是二分图
			} else if (colors[w] == colors[v]) {
				return false;
			}
		}
		return true;
	}
	

	// ================================================================
	// Tests
	// ================================================================
	
	private void beforeTest(String graphText) {
		this.graph = new GraphTable(graphText);
		int vertexNum = graph.V();
		this.visited = new boolean[vertexNum];
		rs = new ArrayList<>(vertexNum);
	}
	
	// 单连通分量图的深度优先遍历
	@Test
	public void testDFS() {
		beforeTest(GRAPH);
		dfs(graph.V());
		System.out.println(rs);
	}

	// 深度优先遍历求无向图的连通分量，连通分量的个数
	@Test
	public void testDFSMulti() {
		// 多连通分量的图
		beforeTest(MULTI);
		dfs(graph.V());
		System.out.println(rs);
		System.out.printf("连通分量的个数为: %d", rs.size());
	}
	
	// 测试单源路径
	@Test
	public void testSingleSourcePath() {
		beforeTest(MULTI);
		System.out.println(singleSourcePath(0, 3));
	}
	// 测试递归无法终止
	@Test
	public void testSingleSourceNoReturn() {
		beforeTest(MULTI);
		singleSourcePathNoReturn(0, 3);
	}
	
	// 检测图的环测试
	@Test
	public void testCycleDetection() {
		beforeTest(MULTI);
		cycleDetected(graph.V());
		System.out.println(rs);
	}
	
	// 二分图检测测试
	@Test
	public void testBinaryGraph() {
		beforeTest(GRAPH);
		System.out.println(binaryGraph(graph.V()));
	}
	
	
	
}
