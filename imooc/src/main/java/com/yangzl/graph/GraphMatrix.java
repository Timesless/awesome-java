package com.yangzl.graph;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author yangzl
 * @date 2020/10/7 15:32
 * @desc 邻接矩阵
 * 		空间复杂度：O(V ^ 2)
 * 		时间复杂度：
 * 			建立图O(E)
 * 			两个节点是否相邻O(1)
 * 			求相邻节点O(V)	
 */
public class GraphMatrix extends AbstractGraph {
	
	// 矩阵
	private int[][] matrix;
	
	// 构造函数
	public GraphMatrix() {
		try {
			List<String> lines = Files.readAllLines(Paths.get("graph.txt"));
			String[] ve = lines.get(0).split(", ");
			V = Integer.parseInt(ve[0]);
			E = Integer.parseInt(ve[1]);
			matrix = new int[V][E];

			lines.stream().skip(1).forEach(line -> {
				String[] vertex = line.split(", ");
				int a = Integer.parseInt(vertex[0]);
				int b = Integer.parseInt(vertex[1]);
				vertexCheck(a, b);
				edgeCheck(a, b);
				// 无向图
				matrix[a][b] = 1;
				matrix[b][a] = 1;
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// 两个顶点是否存在边
	@Override
	public boolean hasEdge(int v1, int v2) {
		vertexCheck(v1, v2);
		return matrix[v1][v2] == 1;
	}
	
	// 返回顶点相邻的顶点
	@Override
	public Collection<Integer> adj(int v) {
		vertexCheck(v);
		return IntStream.range(0, V)
				.filter(i -> matrix[v][i] == 1)
				.boxed()
				.collect(Collectors.toList());
	}
	
	// 返回顶点的度
	@Override
	public int degree(int v) {
		return adj(v).size();
	}

	// toString()
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(0xff);
		sb.append(String.format("V = %d, E = %d\n", V, E));
		for (int i = 0; i < V; ++i) {
			for (int j = 0; j < V; ++j) {
				sb.append(String.format("%d ", matrix[i][j]));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	// ======================================================
	
	// 自环边、平行边检测
	private void edgeCheck(int v1, int v2) {
		// 有权图时，平行边可以选择权重小的
		if (matrix[v1][v2] == 1)
			throw new IllegalArgumentException("Parallel edge does not allow here");
		if (v1 == v2)
			throw new IllegalArgumentException("Self loop edge does not allow here");
	}
	// 顶点合法性检测
	private void vertexCheck(int v1, int v2) {
		vertexCheck(v1);
		vertexCheck(v2);
	}
	private void vertexCheck(int v) {
		if (v < 0 || v > V)
			throw new IllegalArgumentException("Vertex is invalid");
	}
	
	
	@Test
	public void testMatrix() {
		System.out.println(new GraphMatrix());
	}
}
