package com.yangzl;

/**
 * @Author: yangzl
 * @Date: 2020/2/5 15:24
 * @Desc: .. 协变数组类型，和泛型通配符
 */
public class GenericTest {

	static int getArea(Shape[] shapes) {
		int rs = 0;
		for (Shape tmp : shapes) {
			rs += tmp.area();
		}
		return rs;
	}
	// 数组协变性
	public static void main(String[] args) {
		Square[] squares = {new Square(), new Square()};
		System.out.println(getArea(squares));
	}
}

class Shape implements Comparable {
	@Override
	public int compareTo(Object o) { return 0; }
	public int area() {
		return 1;
	}
}
class Square extends Shape {
	@Override
	public int area() {
		return super.area();
	}
	@Override
	public int compareTo(Object o) {
		return 1;
	}
}
class Circle extends Shape {
	@Override
	public int area() {
		return super.area();
	}
}
class Person<T> {
	public void test2(T t) { }
	public static <T> void test(T t) {
		System.out.println(t);
	}
}