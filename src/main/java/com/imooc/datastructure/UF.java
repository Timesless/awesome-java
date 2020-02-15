package com.imooc.datastructure;

/**
 * @Date: 2020/2/15
 * @Desc:  Union Find 并查集， 每个元素只有一个指针，所以可以用数组模拟， t[i] = i
 **/
public interface UF {
	/**
	 * @Date: 2020/2/15
	 * @Desc:  朋友圈
	 **/
	void union(int p, int q);
	
	/**
	 * @Date: 2020/2/15
	 * @Desc:  对于元素具体类型我们是不关心的， 我们只需要关心他们之间是否有联系
	 **/
	boolean isConnected(int p, int q);
}

/**
 * @Date: 2020/2/15
 * @Desc:  Union时间复杂度(Oh), find(Oh)
 * 		union执行多次，会被组织到一棵树中，性能可能变低
 **/
class QuickUnion implements UF {
	private int[] t;
	public QuickUnion(int size) {
		this.t = new int[size];
		// 森林， 每棵树都指向自己
		for (int i = 0; i < size; ++i)
			t[i] = i;
	}
	
	/**
	 * @Date: 2020/2/15
	 * @Desc:  查询当前节点的根节点。O(h)
	 **/
	private int getRoot(int e) {
		while (t[e] != e)
			e = t[e];
		return e;
	}
	
	@Override
	public void union(int p, int q) {
		int pRoot = getRoot(p);
		int qRoot = getRoot(q);
		// p的根节点指向q的根节点
		if (pRoot != qRoot)
			t[pRoot] = qRoot;
	}

	@Override
	public boolean isConnected(int p, int q) { return getRoot(p) == getRoot(q); }
}


/**
 * @Date: 2020/2/15
 * @Desc: 基于rank优化，降低树的高度。每个节点保存一个相对高度rank，union时低的指向高的
 **/
class RankUF implements UF {
	private int[] t;
	private int[] rank;
	public RankUF(int size) {
		this.t = new int[size];
		this.rank = new int[size];
		for (int i = 0; i < size; ++i) {
			t[i] = i;
			// 初始rank为1
			rank[i] = 1;
		}
	}

	/**
	 * @Date: 2020/2/15
	 * @Desc: 基于rank优化执行路径压缩，find时顺便压缩路径parant[p] = parent[parent[p]]
	 * 		不需要维护rank，rank是一个相对高度
	 * 	时间复杂度  log*N
	 * 	将访问路径上所有节点都指向，同一个根节点
	 * 	if(t[e] != e)
	 * 		t[e] = getRoot(t[e]);
	 * 	return t[e];
	 **/
	private int getRoot(int e) {
		while (t[e] != e) {
			// e指向 e父亲的父亲
			t[e] = t[t[e]];
			e = t[e];
		}
		return e;
	}
	
	/**
	 * @Date: 2020/2/15
	 * @Desc:  union维护rank
	 **/
	@Override
	public void union(int p, int q) {
		int pRoot = getRoot(p);
		int qRoot = getRoot(q);
		if (rank[pRoot] < rank[qRoot])
			t[pRoot] = qRoot;
		else if (rank[pRoot] > rank[qRoot])
			t[qRoot] = pRoot;
		else {
			t[pRoot] = qRoot;
			rank[qRoot] ++;
		}
	}

	@Override
	public boolean isConnected(int p, int q) { return getRoot(p) == getRoot(q); }
}
