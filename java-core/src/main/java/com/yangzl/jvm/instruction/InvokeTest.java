package com.yangzl.jvm.instruction;

/**
 * @author yangzl
 * @date 2020/12/30 20:29
 *
 * 方法调用字节码深入理解
 * 
 * ❏ 5条方法调用指令的联系和区别
 * ❏ JVM方法分派机制与vtable、itable原理
 * ❏ 通过HSDB来查看JVM运行时数据
 * ❏ invokedynamic指令的介绍及在Lambda表达式中的作用
 * ❏ 从字节码角度理解泛型擦除
 * ❏ synchronized关键字的字节码原理❏ 反射的底层实现原理
 * 
 * 		invokespecial 私有方法，父类方法，构造方法
 * 			<init>
 * 			private
 * 			super
 * 			
 * 		invokevitural 非私有实例方法 和 final方法
 * 			在运行期确定，属于动态绑定，根据对象实际类型进行分配「vtable，虚方法分配」
 * 			aload_1
 * 			invokevitural #5	// Method java/lang/Object.toString:()Ljava/lang/String;
 * 			
 * 		invokestatic 静态方法
 * 			在编译期确定，属于静态绑定
 * 			ldc		#2	// String 42
 * 			invokestatic	#3 	// Method java/lang/Integer.valueOf:(Ljava/lang/String;)Ljava/lang/Integer;
 * 			
 * 		invokeinterface	接口方法
 * 			在运行期确定，属于动态绑定 「itable」	
 * 			
 * 		invokedynamic 动态方法
 * 			1.7新增字节码指令invokedynamic，为Java8 Lambda式提供服务
 * 			java.io.PrintStream.println("hello world")
 * 				要求调用对象类必须为java.io.PrintStream，方法名必须为println，方法参数必须String，返回值必须void
 * 				如果当前类找不到符合条件的函数，那么去父类查找，如果查找不到则编译不通过
 * 				但相同的代码在Groovy和JavaScript中表现不同，只要类中有println()且参数类型为String，那么就会调用成功
 * 					关注对象的行为而非对象的类型，不关注对象的继承关系
 * 			
 * 			java.lang.invoke.MethodHandle：让Java可以把函数当作参数传递
 * 	
 * 	
 * 	为什么有invokevirtual还会有invokespecial呢？
 * 		处于效率，invokespecial可在编译期确定，而invokevirtual会查虚方法表「vtable」
 * 	
 * 	itable vtable 以数组实现，用来实现多态
 * 	  vtable
 * 		虚方法表中，覆写的方法指向各子类自己的实现，各自新增方法添加到虚方法表末尾
 * 		这样每次在数组的固定offset调用即可，它会指向正确的实现
 * 		
 * 	  itable
 * 	  	itable可以多继承，由偏移量表「offset table」和方法表「method table」组成
 * 	    offset table中保存多个接口「接口多继承」的偏移量，如：
 * 	    	offset table entry interface #1
 * 	    	offset table entry interface #2
 * 	    	offset table entry interface #3
 * 	    method table
 * 	    	vtable for interface #1
 * 	    	vtable for interface #2
 * 	    	vtable for interface #3
 * 	    每一个vtable同上vtable结构
 * 	    
 * 	❏ vtable、itable机制是实现Java多态的基础。
 *  ❏ 子类会继承父类的vtable。Object中有5个方法可以被继承，所以一个空Java类的vtable的大小也等于5。
 *  ❏ 被final和static修饰的方法不会出现在vtable中，因为没有办法被继承重写，同理可以知道private。
 *  ❏ 接口方法的调用使用invokeinterface指令，Java使用itable来支持多接口实现，itable由offset table和methodtable两部分组成。
 *  	在调用接口方法时，会先在offset table中查找method table的偏移量位置，随后在method table查找具体的接口实现。
 */
public class InvokeTest {
	
	
	/**
	 * 对应虚方法表为
	 * class A				class B
	 * index	方法引用		index		方法引用
	 * 1		A/m1		1			A/m1
	 * 2		A/m2		2			B/m2
	 * 3		A/m3		3			A/m3
	 * 						4			B/m4
	 */
	static class A {
		public void m1(){}
		public void m2(){}
		public void m3(){}
		
	}
	static class B extends A {
		@Override
		public void m2() {
			// tips
			super.m2();
		}
		public void m4() {}
	}
	
	
	/**
	 * itable
	 * 
	 * class E					class F
	 * index	method ref		index	method ref
	 * 1		m1				1		m3
	 * 2		m2
	 * 3		m3
	 * 
	 * public void foo(B b) {
	 *     b.m3()
	 * }
	 * E中m3在itable的第三个位置，F类中m3在itable第一个位置
	 * invokevirtual在固定偏移获取不到对应方法，因此只能搜索整个itable找到对应方法，使用invokeinterface
	 */
	interface C {
		/** m1 */
		void m1();
		/** m2 */
		void m2();
	}
	interface D {
		/** m3 */
		void m3();
	}
	static class E implements C, D {
		@Override
		public void m1() {
			
		}
		@Override
		public void m2() {

		}
		@Override
		public void m3() {

		}
	}
	static class F implements D {
		@Override
		public void m3() {
			
		}
	}

}
