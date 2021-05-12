package com.yangzl.graph;

import java.util.Collection;

/**
 * @author yangzl
 * @date 2020/10/7 15:12
 *
 * 	顶点 Vertex
 *		顶点的度（响铃的边数）
 *			出度
 *			入读	
 * 	
 * 	边 Edge
 * 		自环边
 * 		平行边
 * 	无自环边，无平行边的图称为简单图
 * 	
 * 	联通分量：一个图所有顶点并不是全部相连的，所以一个图可以存在多个连通分量
 * 	一个联通的无环图一定是一棵树
 * 		一个连通图有对应生成树（生成树最少边为V - 1）
 * 
 * 	有无权重、有无方向
 * 		无向无权图
 * 		无向有权图
 * 		有向无权图（DAG）
 * 		有向有权图
 * 	
 * 	*******************************8
 * 	图的表示：
 * 		邻接矩阵
 * 		邻接表
 * 			LinkedList
 * 			TreeSet
 * 			HashSet
 *
 * 	无向无权图
 */

public interface Graph {

	/**
	 * 两个顶点是否联通
	 *
	 * @param v1 v1
	 * @param v2 2
	 * @return bool
	 */
	boolean hasEdge(int v1, int v2);

	/**
	 *  顶点的相邻边
	 *
	 * @param v v
	 * @return list
	 */
	Collection<Integer> adj(int v);

	/**
	 * 返回该顶点的度
	 *
	 * @param v v
	 * @return int
	 */
	int degree(int v);

	/**
	 * 返回图多少顶点
	 *
	 * @return int
	 */
	int V();

	/**
	 * 返回图有多少边
	 *
	 * @return int
	 */
	int E();
}
