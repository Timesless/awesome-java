package com.yangzl.graph;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

/**
 * @author yangzl
 * @date 2020/10/7 17:35
 *
 * 邻接表
 * 	空间复杂度：O(V + E)顶点 + 边
 * 	时间复杂度：
 * 	LinkedList:
 * 		建立图：如果判断自环边 / 平行边 那么为：O(E * V)，如果不判断那么为O(E)
 * 		hasEdge 时间复杂度：O(dgree(v))
 * 		求一个节点的相邻节点：O(dgree(v))，虽然已经缓存但我们还是需要遍历一遍呢
 * 	TreeSet:
 * 		建立图：如果判断自环边 / 平行边 那么为：O(E * log(V))，如果不判断那么为O(E)
 * 		hasEdge 时间复杂度：O(log(dgree(v)))
 * 		求一个节点的相邻节点：O((dgree(v)))，虽然已经缓存但我们还是需要遍历一遍呢
 * 	HashSet:
 * 		建立图：如果判断自环边 / 平行边 那么为：O(E)，如果不判断那么为O(E)
 * 		hasEdge 时间复杂度：O(1)
 * 		求一个节点的相邻节点：O(dgree(v))，虽然已经缓存但我们还是需要遍历一遍呢
 */
public class GraphTable extends AbstractGraph {
	
	/** 邻接表实现图 */
	private TreeSet<Integer>[] table;
	
	public GraphTable(String graphText) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(graphText));
			String[] ve = lines.get(0).split(", ");
			V = Integer.parseInt(ve[0]);
			E = Integer.parseInt(ve[1]);
			/*
			 * Java语法的缺陷，无法new泛型数组
			 * 这里有多种实现：
			 * 	LinkedList 查找时间复杂度 O(V)
			 * 	table = new LinkedList[V];
				for (int i = 0; i < V; ++i) {
					table[i] = new LinkedList<>();
				}
			 * 	TreeSet	查找时间复杂度O(log(dgree(v))，不浪费空间
			 * 	HashSet	查找时间复杂度O(1)，比较浪费空间，且存储的元素无序
			 * 	table = new HashSet[V];
				for (int i = 0; i < V; ++i) {
					table[i] = new HashSet<>();
				}
			 */
			table = new TreeSet[V];
			for (int i = 0; i < V; ++i) {
				table[i] = new TreeSet<>();
			}
			lines.stream().skip(1).forEach(line -> {
				String[] vertex = line.split(", ");
				int a = Integer.parseInt(vertex[0]);
				int b = Integer.parseInt(vertex[1]);
				vertexCheck(a, b);
				edgeCheck(a, b);
				table[a].add(b);
				table[b].add(a);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasEdge(int v1, int v2) {
		vertexCheck(v1, v2);
		return table[v1].contains(v2);
	}

	@Override
	public Collection<Integer> adj(int v) {
		vertexCheck(v);
		return table[v];
	}

	@Override
	public int degree(int v) {
		// return adj(v).size()
		vertexCheck(v);
		return table[v].size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(0xff);
		sb.append(String.format("V = %d, E = %d\n", V, E));
		for (int i = 0; i < V; ++i) {
			sb.append(String.format("%d: ", i));
			sb.append(table[i]);
			sb.append("\n");
		}
		return sb.toString();
	}

	
	// ============================================================
	// divide
	// ============================================================
	

	/** 自环边、平行边检测 */
	private void edgeCheck(int v1, int v2) {
		
		// 其实对于无向无权图来说，这个边没有任何影响
		if (table[v1].contains(v2))
			throw new IllegalArgumentException("Parallel edge does not allow here");
		if (v1 == v2)
			throw new IllegalArgumentException("Self loop edge does not allow here");
	}
	/** 顶点合法性检测 */
	private void vertexCheck(int v1, int v2) {
		vertexCheck(v1);
		vertexCheck(v2);
	}
	private void vertexCheck(int v) {
		if (v < 0 || v > V)
			throw new IllegalArgumentException("Vertex is invalid");
	}
	
	
	@Test
	public void test() {
		System.out.println(new GraphTable("graph.txt"));
	}
}
