package com.yinhai.datastrcture.tree.avl;

/**
 * @Author: yangzl
 * @Date: 2019/12/3 20:04
 * @Desc: ..
 **/
public class AvlTree {

	private AvlNode root;
	public AvlTree() {}
	public AvlTree(AvlNode root) { this.root = root; }
	
	public AvlNode getRoot() {
		return this.root;
	}
	
	/**
	 * @Date: 2019/12/3 计算树的高度，可作为AvlNode属性提供
	 * @Desc: ..如果作为属性提供，每次添加 | 删除例程之后需调整树的高度
	 **/
	public int height() {
		return height(root);
	}
	private int height(AvlNode node) {
		return null == node ? 0 : Math.max(height(node.left), height(node.right)) + 1;
	}
	
	/**
	 * @Date: 2019/12/3 查找值最小的节点
	 * @Desc: .. 必定是往当前节点的左子树去查找
	 **/
	public AvlNode findMin(AvlNode node) {
		if (null == node) {
			return null;
		} else if (null == node.left) {
			return node;
		}
		return findMin(node.left);
	}
	/**
	 * @Date: 2019/12/3 查找值最大的节点
	 * @Desc: .. 必定是往当前节点的右子树去查找
	 **/
	public AvlNode findMax(AvlNode node) {
		if (null == node) {
			return null;
		} else if (null == node.right) {
			return node;
		}
		return findMax(node.right);
	}

	/**
	 * @Date: 2019/12/3 提供中序遍历
	 * @Desc: 
	 **/
	public void infixOrder() {
		if (null == root) { return; }
		infixOrder(this.root);
	}
	private void infixOrder(AvlNode node) {
		if (null != node.left) { infixOrder(node.left); }
		System.out.print(node.val + " ");
		if (null != node.right) { infixOrder(node.right); }
	}
	
	/**
	 * @Date: 2019/12/3 添加节点
	 * @Desc:
	 **/
	public void add(int val) { root = add(val, root); }
	private AvlNode add(int val, AvlNode node) {
		if (null == node) { return new AvlNode(val); }
		if (val < node.val) {
			node.left = add(val, node.left);
		} else if (val > node.val){
			node.right = add(val, node.right);
		} else
			;	// duplicate; do nothing
		/*
		 * 执行旋转保持树的高度平衡，可返回 | 不返回值
		 * 第一种方式 复杂一点的不返回值，
		 * 	banlance(node);
		 *	return node;
		 * 第二种方式,例程请在《数据结构与算法分析》p93查看
		 * 	return banlanceWithReturn(node);
		 */
		banlance(node);
		return node;
	}
	
	/**
	 * @Date: 2019/12/3 平衡二叉树的例程
	 * @Desc: .. 存在以下情况：
	 * 			1. 在当前节点左子树的左子节点插入导致不平衡 右旋
	 * 			2. 在当前节点左子树的右子节点插入导致不平衡 左右双旋 =》 先将当前节点的左子树左旋，再将当前节点的树右旋
	 * 			3. 在当前节点右子树的左子节点插入导致不平衡 右左双旋 =》 先将当前节点的右子树右旋，再将当前节点的树左旋
	 * 			4. 在当前节点右子树的右子节点插入导致不平衡 左旋
	 **/
	private void banlance(AvlNode node) {
		// 当前节点左子树高度 - 当前节点右子树高度 > BANLANCE_FACTOR
		if (height(node.left) - height(node.right) > 1) {
			// 在左子树的左子节点插入
			if (height(node.left.left) >= height(node.left.right)) {
				rightRotate(node);
			}
			// 这里为什么不写成else是因为删除例程也会调用banlance
			// 在左子树的右子节点插入
			if (height(node.left.right) > height(node.left.left)) {
				leftRotate(node.left);
				rightRotate(node);
			}
		}
		// 当前节点右子树的高度 - 当前节点左子树的高度 > BANLANCE_FACTOR
		if (height(node.right) - height(node.left) > 1) {
			// 在右子树的右子节点插入
			if (height(node.right.right) >= height(node.right.left)) {
				leftRotate(node);
			}
			// 在右子树的左子节点插入
			if (height(node.right.left) > height(node.right.right)) {
				rightRotate(node.right);
				leftRotate(node);
			}
		}
	}
	/**
	 * @Date: 2019/12/3 无返回值的左旋例程
	 * @Desc:  1
	 * 		  /	\
	 * 	   null	 2
	 * 			/  \
	 * 		null	3
	 *	.. 当前节点，即node，即上图的1
	 **/
	private void leftRotate(AvlNode node) {
		System.out.println("执行左旋...");
		AvlNode rt = node.right;
		// 以当前节点的值创建一个新节点
		AvlNode newLeftNode = new AvlNode(node.val);
		// 新节点的左子树设置为当前节点的左子树
		newLeftNode.left = node.left;
		// 新节点的右子树设置为当前节点右子树的左子树
		newLeftNode.right = rt.left;
		// 当前节点的值用当前节点的右子节点的值替换
		node.val = node.right.val;
		// 当前节点的右子树设置为当前节点右子树的右子树
		node.right = rt.right;
		// 当前节点的左子树设置为新节点
		node.left = newLeftNode;
	}
	/**
	 * @Date: 2019/12/3 无返回值的右旋例程
	 * @Desc: ..  1
	 * 			 /
	 * 			2
	 * 		   /
	 * 		  3
	 **/
	private void rightRotate(AvlNode node) {
		AvlNode lt = node.left;
		System.out.println("执行右旋...");
		AvlNode newRightNode = new AvlNode(node.val);
		newRightNode.left = lt.right;
		newRightNode.right = node.right;
		// 用当前节点左子节点的值替换当前节点的值
		node.val = node.left.val;
		// 当前节点的左子树 = 当前节点左子树的左子树
		node.left = lt.left;
		node.right = newRightNode;
	}

	/**
	 * @Date: 2019/12/8 
	 * @Desc: .. 以下代码为另一种实现方式，参考自数据结构与算法分析
	 **/
	private AvlNode banlanceWithReturn(AvlNode node) {
		// 当前节点左子树高度 - 当前节点右子树高度 > BANLANCE_FACTOR
		if (height(node.left) - height(node.right) > 1) {
			// 在左子树的左子节点插入
			if (height(node.left.left) > height(node.left.right)) {
				node = rotateWithLeft(node);
			}
			// 在左子树的右子节点插入
			if (height(node.left.right) > height(node.left.left)) {
				// 这里可以定义一下k1，k1永远在左边
				AvlNode k1 = node.left;
				node.left = rotateWithRight(k1);
				node = rotateWithLeft(node);
			}
		}
		// 当前节点右子树的高度 - 当前节点左子树的高度 > BANLANCE_FACTOR
		if (height(node.right) - height(node.left) > 1) {
			// 在右子树的右子节点插入
			if (height(node.right.right) > height(node.right.left)) {
				node = rotateWithRight(node);
			}
			// 在右子树的左子节点插入
			if (height(node.right.left) > height(node.right.right)) {
				// 这里可以定义一下k2，或者不定义
				AvlNode k2 = node.right;
				node.right = rotateWithLeft(k2);
				node = rotateWithRight(node);
			}
		}
		return node;
	}
	/**
	 * @Date: 2019/12/3 有返回值的左旋例程
	 * @Desc: .. 左旋传入k2
	 * 		1
	 * 		 \
	 * 		  2
	 * 		   \
	 * 		    3
	 * .. k1即1，首先获取k2 = k1.right，那么获取之后k1.right就应该改变了，变成k2.left。改变之后 1 2断开链接，所以应该使用
	 * 	k2.left = k1;
	 **/
	private AvlNode rotateWithRight(AvlNode k1) {
		System.out.println("执行左旋...");
		AvlNode k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		return k2;
	}
	/**
	 * @Date: 2019/12/3 有返回值的右旋例程
	 * @Desc: 
	 **/
	private AvlNode rotateWithLeft(AvlNode k2) {
		System.out.println("执行右旋...");
		AvlNode k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		return k1;
	}

}
