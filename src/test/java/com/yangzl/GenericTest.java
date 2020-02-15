package com.yangzl;

import java.util.ArrayList;

/**
 * @Author: yangzl
 * @Date: 2020/2/5 15:24
 * @Desc: .. 协变数组类型，和泛型通配符
 * 	泛型是
 */
public class GenericTest {
	public static void main(String[] args) {
		/**
		 * 						Food
		 *					  /      \
		 * 				   Fruit	  Meat
		 * 				   /   \      /   \
		 *				Apple Banana Pork Beef
		 *             /   \
		 * 			Green  Red
		 * 上界通配符 => 协变	
		 * 此时ArrayList<? extends Fruit>能获取Fruit在内的左下角所有类型（都以Fruit接收）
		 * 		是ArrayList<Fruit, Apple, Banana, GreenApple, RedApple>的父类
		 * 		即可以new这些子类，new ArrayList<GreenApple>
		 * 此通配符限制add|set，因为编译器不知道是哪种子类被添加（避免运行时CCE）null可以
		 * get是有效的，我们都以Fruit接收（LSP引用基类的地方可透明引用子类）
		 */
		ArrayList<? extends Fruit> lowerBound = new ArrayList<GreenApple>();
		lowerBound.add(null);
		Fruit fruit = lowerBound.get(0);
		/**
		 * 下界通配符 => 逆变
		 * <ArrayList ? super Fruit>能添加Fruit在内的左下角所有
		 * 		是ArrayList<Fruit, Food, Object>的父类
		 * 		即可以new这些，new ArrayList<Food>
		 * 此通配符限制get，编译器不知道哪种父类被get，所以只能以Object获取
		 * add|set是有效的，因为我们都当作Fruit去add|set（LSP引用基类的地方可透明引用子类）
		 **/
		ArrayList<? super Fruit> upperBound = new ArrayList<Food>();
		upperBound.add(new Fruit());
		upperBound.add(new Apple());
		upperBound.add(new Banana());
		upperBound.add(new GreenApple());
		// 只能以Object获取
		Object o = upperBound.get(0);
		
		/*
		 * PECS
		 * Collections.copy();
		 * public static <T> void copy(List<? super T> dest, List<? extends T> src) {
				int srcSize = src.size();
				if (srcSize < COPY_THRESHOLD ||
					(src instanceof RandomAccess && dest instanceof RandomAccess)) {
					for (int i=0; i<srcSize; i++)
						dest.set(i, src.get(i));
				} else {
					ListIterator<? super T> di=dest.listIterator();
					ListIterator<? extends T> si=src.listIterator();
					for (int i=0; i<srcSize; i++) {
						di.next();
						di.set(si.next());
					}
				}
			}
		 */
		
		/*
		 * <?>无界通配符，用于读取，只能持有一种类型
		 * List<Object>，可以持有任何类型
		 */
		ArrayList<Object> objs = new ArrayList<>();
		ArrayList<?> general = new ArrayList<>();
		Object o2 = general.get(1);
		
		/*
		 * 不能new一个确切的泛型数组，考虑以下例程
		 *  List<String>[] lsa = new List<String>[10];
			Object o = lsa;
			Object[] oa = (Object[]) o;
			List<Integer> li = new ArrayList<Integer>();
			li.add(new Integer(3));
			// Unsound, but passes run time store check
			oa[1] = li;
			String s = lsa[1].get(0); //ClassCastException
		 * 所以编译器不允许这样创建
		 * 
		 * 可以通过通配符创建，get时都以Object获取
		 */
		ArrayList<String>[] genericStringArray = new ArrayList[10];
		ArrayList<?>[] genericArray = new ArrayList<?>[10];
	}
}
class Food {}

class Meat extends Food {}
class Fruit extends Food {}

class Pork extends Meat {}
class Beef extends Meat {}

class Banana extends Fruit {}
class Apple extends Fruit {}

class RedApple extends Apple {}
class GreenApple extends Apple {}



