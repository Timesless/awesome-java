package com.yangzl.datastrcture.tree.heap;

/**
 * @Author: yangzl
 * @Date: 2019/12/7 21:41
 * @Desc: .. 二叉堆，是一颗完全二叉树，可由顺序存储二叉树数组实现
 * 			二叉堆ADT ： 最小堆 findMin(), deleteMin(), insert()
 * 						最大堆 findMax(), deleteMax(), insert()
 * 	我们可以使数组第0位置不存储值，那么对数组任意位置i，其左儿子位置为 i << 1，右儿子 (i << 1) + 1，父节点 i >>> 1
 * 	如果第0个位置存储值，那么任意位置i，其左儿子 (i << 1) + 1，右儿子(i << 1) + 2，父节点 (i - 1) >>> 1
 * 	  这里我们采用第一种策略
 **/
public class BinaryHeap {
	
	private int[] arr;
	private int curSize;
	
	// 构造函数
	public BinaryHeap(int[] arr) {
		this.curSize = arr.length;
		this.arr = new int[(arr.length << 1) + 1];
		int x = 0;
		for (; x < arr.length; ++x) { this.arr[x + 1] = arr[x]; }
		this.buildHeap();
	}
	
	/**
	 * @Date: 2019/12/7 将无序的数组构建为堆，我们这里构建最小堆
	 * @Desc: 
	 **/
	private void buildHeap() {
		for (int x = curSize >>> 1; x > 0; --x) { percolateDown(x); }
	}
	
	/**
	 * @Date: 2019/12/7 获取最小元
	 * @Desc: 
	 **/
	public int findMin() { return arr[1]; }
	
	/**
	 * @Date: 2019/12/7 删除最小元
	 * @Desc: .. 最小堆删除最小元，需要在根节点建立 hole， 现在堆中少了一个元素，因此堆中最后一个元素x，必须移动到某个位置
	 * 				我们将 hole的较小儿子移入 hole，让 hole下沉，重复此步骤直到找到x被放入hole，这种策略叫做下滤
	 * 			percolateDown	
	 **/
	public int deleteMin() {
		int min = arr[1];
		arr[1] = arr[curSize--];
		// 从根开始下滤（将hole往下沉，直到x能放入hole）
		percolateDown(1);
		return min;
	}
	
	/**
	 * @Date: 2019/12/7 添加元素
	 * @Desc: .. 在堆下一个可用位置建立 hole，否则该堆不是完全二叉树，如果x可以放入hole那么插入完成，否则
	 * 			把 hole 父节点的值放入hole，hole上移，这种策略叫上滤 percolateUp
	 * 		这里我们没有考虑扩容。	
	 **/
	public void insert(int val) {
		int hole = ++curSize;
		for (; val < arr[hole >>> 1]; hole = hole >>> 1)
			arr[hole] = arr[hole >>> 1];
		arr[hole] = val;
	}
	
	/**
	 * @Date: 2019/12/7 打印当前堆
	 * @Desc: 
	 **/
	public void printHeap() {
		int x = 1;
		for (; x <= curSize; ++x) { System.out.print(arr[x] + " "); }
	}
	
	/**
	 * @Date: 2019/12/7 将idx节点的值与以idx位置为父节点的较小儿子节点的值交换
	 * @Desc: .. 
	 * 		70				50
	 * 	   /  \		=>     /  \
	 * 	  50  120		 70	  120
	 **/
	private void percolateDown(int hole) {
		int tmp = arr[hole], child;
		for (; (hole << 1) <= curSize; hole = child) {
			child = hole << 1;
			// 找到较小的子节点
			if (child != curSize && arr[child + 1] < arr[child]) { ++child; }
			if (arr[child] < tmp) {
				arr[hole] = arr[child];
			} else
				break;
		}
		arr[hole] = tmp;
	}
	
	/**
	 * @Date: 2019/12/7 堆排序
	 * @Desc: .. 每次deleteMin之后，堆缩小1，此时位于堆中最后的单元可以用来存放刚刚删除的元素
	 * 			升序序列需要构建最大堆， 降序序列需要构建最小堆
	 **/
	public void heapSort() {
		int x , min;
		for (x = curSize; x > 0; --x) {
			min = this.deleteMin();
			arr[x] = min;
		}
		for (int y = 1; y < arr.length; ++y)
			if (arr[y] != 0)
				System.out.print(arr[y] + " ");
	}


	public static void main(String[] args) {
		int[] arr = {4, 6, 8, 5, 9, -1, 99};
		BinaryHeap heap = new BinaryHeap(arr);
		heap.printHeap();
		System.out.println();
		heap.heapSort();
	}

}
