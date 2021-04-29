package com.yangzl.graph;

import java.util.Collection;

/**
 * @author yangzl
 * @date 2020/10/7 17:35
 */
public abstract class AbstractGraph implements Graph {

	/** 顶点 */
	protected int V;
	/** 边 */
	protected int E;

	@Override
	public int V() { return V; }

	@Override
	public int E() { return E; }

	/**
	 * 两个顶点之间是否存在边「是否相连」
	 *
	 * @param v1 v1
	 * @param v2 v2
	 * @return bool
	 */
	public abstract boolean hasEdge(int v1, int v2);

	public abstract Collection<Integer> adj(int v);

	public abstract int degree(int v);
}
