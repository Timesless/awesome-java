package com.yangzl.onjava8;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;

/**
 * @Author: yangzl
 * @Date: 2020/5/6 11:01
 * @Desc: ..
 */
public enum Enum22 {
	
	/*
	 * 1 枚举的枚举，用法 TODO
	 */
	FOOD(EI.Food.class), DRINK(EI.Drink.class);
	
	// 当作接口来使用
	EI[] eis;
	Enum22(Class<? extends EI> clzz) { eis = clzz.getEnumConstants(); }
	
	/*
	 * 2 用接口管理枚举
	 */
	interface EI {
		enum Food implements EI {
			SALAD("沙拉") {
				@Override
				String getInfo() { return "我是".concat(desc); }
				@Override
				public String toString() { return "我是覆盖的".concat(desc); }
			},
			CHESS("奶酪") {
				@Override
				String getInfo() { return "我是".concat(desc); }
			};

			String desc;
			Food(String desc) { this.desc = desc; }
			
			// 3 EnumMap，k，v都可以使用枚举
			private static final EnumMap<Food, Integer> maps = new EnumMap<>(Food.class);
			static {
				for (Food e :Food.class.getEnumConstants())
					maps.put(e, 4);
			}
			// 提供访问map
			public static int getEVal(Food f) { return maps.get(f); }
			
			// 4 可以提供多个抽象方法
			abstract String getInfo();
			// 实例方法
			@Override
			public String toString() { return desc; }
		}
		
		enum Drink implements EI {
			COFF("咖啡"), TEA("茶饮"), MILK("牛奶");
			String desc;
			Drink(String desc) { this.desc = desc; }
		}
	}
	
	// TODO 5.实现状态机，多路分发
	
	/*
	 * EnumSet，EnumMap
	 */
	public static void main(String[] args) {
		System.out.println(Arrays.toString(EI.Food.values()));
		System.out.println(Arrays.toString(EI.Drink.values()));

		EnumSet<EI.Food> set = EnumSet.allOf(EI.Food.class);
		System.out.println(set);
		System.out.println(EI.Food.getEVal(EI.Food.CHESS));
	}
}
