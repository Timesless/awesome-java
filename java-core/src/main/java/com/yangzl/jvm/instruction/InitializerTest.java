package com.yangzl.jvm.instruction;

/**
 * @author yangzl
 * @date 2020/12/30 20:14
 *
 * 对象的创建字节码指令
 * 		new「JVM层面，分配内存空间初始化0值」
 * 		dup「dup的本质是<init>无返回值，会消耗一个引用」
 * 		invokespecial「<init>()，用户层面构造函数调用」
 * 	
 * 	实例变量复制和构造块代码都会被收集到<init>()中
 * 	静态变量和静态块中代码都会被收集到<clinit>()「static {}」中
 * 	
 * 	javap输出字节码中的static {} 表示＜clinit>方法。
 * 	＜clinit>不会直接被调用，它在四个指令触发时被调用（new、getstatic、putstatic、invokestatic
 * 	
 * 		1. 创建类对象的实例，比如new、反射、反序列化
 * 		2. 访问类静态变量或静态方法
 * 		3. 访问类的静态字段或对静态字段赋值「final字段除外，static final为常量」
 * 		4. 初始化类的某个子类	
 */
public class InitializerTest {
	
	/** 实例变量 */
	private final int a = 10;
	/** 构造函数 */
	public InitializerTest() {
		int c = 30;
	}
	
	// 构造块
	{
		int b = 20;
	}
	
	/** 静态变量 */
	private static final int d = 40;
	
	// 静态块
	static {
		int e = 50;
	}
	
	
	
	
	

}
