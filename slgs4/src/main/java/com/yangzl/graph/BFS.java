package com.yangzl.graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

/**
 * @author yangzl
 * @date 2020/10/8 14:27
 * @desc 图的广度优先遍历及应用
 * 
 * 		BFS 的重要特性：我们求出来的是一条最短路径（我们不可能后遍历一个节点，这个节点比前面节点到源节点的距离还短）
 * 			我们后遍历的节点是基于前面的节点的	
 * 
 * 	时间复杂度 O(V + E)
 * 	multi
 *             0
 *         /      \
 *      1  ——  3 —— 2
 *       \			\
 * 		  4		5	 6
 */
public class BFS {

	Graph graph;
	List<List<Integer>> rs;
	boolean[] visited;
	
	
	/**
	 * 2020/10/8 
	 * 
	 * @param vertexNum 图的节点数
	 */
	public void BFS(int vertexNum) {
		// 可能存在多个连通分量
		for (int v = 0; v < vertexNum; ++v) {
			if (!visited[v]) {
				List<Integer> list = new ArrayList<>(vertexNum);
				BFS0(list, v);
				rs.add(list);
			}
		}
	}
	private void BFS0(List<Integer> list, int v) {
		Queue<Integer> q = new ArrayDeque<>(list.size());
		q.offer(v);
		visited[v] = true;
		while (!q.isEmpty()) {
			int cur = q.poll();
			// 请注意这里 visited 赋值为true的时机
			// visited[cur] = true 不行
			list.add(cur);
			for (int w : graph.adj(cur)) {
				if (!visited[w]) {
					visited[w] = true;
					q.offer(w);
				}
			}
		}
	}
	
	/**
	 * 2020/10/8 单源路径问题 BFS 
	 * 
	 *             0
	 *         /      \
	 *      1  ——  3 —— 2
	 *       \			\
	 * 		  4		5	 6
	 * @param v1 源顶点
	 * @param v2 目标顶点   
	 * @return list
	 */
	public List<Integer> singleSourcePath(int v1, int v2) {
		
		int sz = graph.V();
		List<Integer> rs = new ArrayList<>(sz);
		Queue<Integer> q = new ArrayDeque<>(sz);
		q.offer(v1);
		visited[v1] = true;
		while (!q.isEmpty()) {
			int cur = q.poll();
			rs.add(cur);
			if (cur == v2) {
				// return rs;
				break;
			}
			for (int w : graph.adj(cur)) {
				if (!visited[w]) {
					visited[w] = true;
					q.offer(w);
				}
			}
		}
		// 如果不连通
		if (!visited[v2]) {
			return Collections.emptyList();
		}
		return rs;
	}
	
	
	/**
	 * 2020/10/8 连通分量个数 BFS 
	 * 
	 * TODO
	 * @param vertexNum 图顶点个数
	 */
	public void BFScount(int vertexNum) {
		
	}
	
	/**
	 * 2020/10/8 二分图检测 BFS 
	 * 
	 * TODO
	 * @param  vertexNum 图顶点个数
	 * @return boolean
	 */
	public boolean binaryGraph(int vertexNum) {
		
		return false;
	}
	
	/**
	 * 2020/10/8 环检测 
	 * 
	 * TODO
	 * @param vertexNum 顶点个数
	 */
	public void loopDetected(int vertexNum) {
		
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
	
	/** 图的广度优先遍历 测试 */
	@Test
	public void testBFS() {
		beforeTest(DFS.MULTI);
		BFS(graph.V());
		System.out.println(rs);
	}
	
	/** 单源路径 广度优先遍历 测试 */
	@Test
	public void testSingleSourcePath() {
		beforeTest(DFS.MULTI);
		System.out.println(singleSourcePath(0, 6));
		// System.out.println(singleSourcePath(0, 2));
	}
}
