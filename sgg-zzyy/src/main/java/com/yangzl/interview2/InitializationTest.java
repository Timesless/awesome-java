package com.yangzl.interview2;

/**
 * @author yangzl
 * @date 2021/3/19
 * 		类初始化与实例初始化过程
 *			1. main 所在的类会初始化
 *			2. 类初始化就是执行 <clinit>()
 *			3. 实例初始化就是执行 <init>()
 *			4. 子类构造器一定会调用父类构造器
 *
 *	本题考察知识点：  1. 类加载顺序
 *					2. 实例构造顺序
 *					3. 方法重写
 */
public class InitializationTest {

	static class F {
		private int a = instanceMethod();
		private static int j = staticMethod();
		static {
			System.out.print(" 1 ");
		}
		F() {
			System.out.print(" 2 ");
		}
		{
			System.out.print(" 3 ");
		}

		/**
		 * 注意这里会产生方法重写
		 *
		 * @return int
		 */
		public int instanceMethod() {
			System.out.print(" 4 ");
			return 1;
		}
		public static int staticMethod() {
			System.out.print(" 5 ");
			return 1;
		}
	}

	static class S extends F {
		private int a = instanceMethod();
		private static int b = staticMethod();
		static {
			System.out.print(" 6 ");
		}
		S() {
			System.out.print(" 7 ");
		}
		{
			System.out.print(" 8 ");
		}

		@Override
		public int instanceMethod() {
			System.out.print(" 9 ");
			return 1;
		}
		public static int staticMethod() {
			System.out.print(" 10 ");
			return 1;
		}
	}

	public static void main(String[] args) {
		//  5  1  10  6  9  3  2  9  8  7
		S s1 = new S();
		System.out.println();
		// 9  3  2  9  8  7
		S s2 = new S();
	}
}
