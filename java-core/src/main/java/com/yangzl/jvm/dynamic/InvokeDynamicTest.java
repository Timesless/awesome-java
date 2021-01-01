package com.yangzl.jvm.dynamic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author yangzl
 * @date 2020/11/7 13:05
 * @desc
 * 
 * 	invokedynamic ==> 关注对象的行为而非对象的类型，不关注对象的继承关系
 * 		lambda本质是前端编译器将lambda编译为静态方法，并为每一个lambda生成BootstrapMethods
 * 		LambdaMetafactory.metafctory 通过 ASM字节码技术动态生成内部类，且实现了对应的函数式接口
 * 		使用 javac -encoding utf8 com/yangzl/jvm/InvokeDynamicTest.java
 * 			java -Djdk.internal.lambda.dumpProxyClasses=. com/yangzl/jvm/InvokeDynamicTest 会生成lambda调用的内部类
 * 		由此可见：每一个lambda表达式都会生成一个内部类
 */
public class InvokeDynamicTest {

	String add(String a, String b) {
		return a + b;
	}
	
	/**
	 * MethodHandle => 让Java可以将函数当作参数传递
	 * 
	 */
	static void methodHandleTest() throws Throwable {
		InvokeDynamicTest client = new InvokeDynamicTest();
		MethodType mt = MethodType.methodType(String.class, String.class, String.class);
		MethodHandle mh = MethodHandles.lookup().findVirtual(InvokeDynamicTest.class, "add", mt);
		String rs = (String) mh.invokeExact(client, "hello", " dynamic");
		System.out.println(rs);
	}
	
	public static void main(String[] args) throws Throwable {
		// methodHandleTest();

		Consumer<String> c = System.out::println;
		Function<String, Integer> f = String::length;
		Supplier<String> s = () -> "hello";
		Predicate<String> p = str -> str.length() > (s.get().length());
		c.accept("hello");
		f.apply("hello");
		p.test("hello");
		Arrays.asList("1", "2", "4", "8").forEach(System.out::println);
	}
	
	
	/**
	 * 2020/11/7
	 * 
	 * @param () 无参
	 * @return void
	 * 
	 * private static boolean lambda$testLambda$3(java.lang.String);
	 *     descriptor: (Ljava/lang/String;)Z
	 *     flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC
	 *     Code:
	 *       stack=2, locals=1, args_size=1
	 *          0: aload_0
	 *          1: ldc           #23                 // String h
	 *          3: invokevirtual #24                 // Method java/lang/String.contains:(Ljava/lang/CharSequence;)Z
	 *          6: ireturn
	 *       LineNumberTable:
	 *         line 43: 0
	 *       LocalVariableTable:
	 *         Start  Length  Slot  Name   Signature
	 *             0       7     0   str   Ljava/lang/String;
	 *
	 *   private static java.lang.String lambda$testLambda$2();
	 *     descriptor: ()Ljava/lang/String;
	 *     flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC
	 *     Code:
	 *       stack=1, locals=0, args_size=0
	 *          0: ldc           #25                 // String str
	 *          2: areturn
	 *       LineNumberTable:
	 *         line 41: 0
	 *
	 *   private static java.lang.Integer lambda$testLambda$1(java.lang.String);
	 *     descriptor: (Ljava/lang/String;)Ljava/lang/Integer;
	 *     flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC
	 *     Code:
	 *       stack=1, locals=1, args_size=1
	 *          0: aload_0
	 *          1: invokevirtual #26                 // Method java/lang/String.length:()I
	 *          4: invokestatic  #27                 // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
	 *          7: areturn
	 *       LineNumberTable:
	 *         line 39: 0
	 *       LocalVariableTable:
	 *         Start  Length  Slot  Name   Signature
	 *             0       8     0   str   Ljava/lang/String;
	 *
	 *   private static void lambda$testLambda$0(java.lang.String);
	 *     descriptor: (Ljava/lang/String;)V
	 *     flags: ACC_PRIVATE, ACC_STATIC, ACC_SYNTHETIC
	 *     Code:
	 *       stack=6, locals=1, args_size=1
	 *          0: getstatic     #17                 // Field java/lang/System.out:Ljava/io/PrintStream;
	 *          3: ldc           #28                 // String consume: %s
	 *          5: iconst_1
	 *          6: anewarray     #29                 // class java/lang/Object
	 *          9: dup
	 *         10: iconst_0
	 *         11: aload_0
	 *         12: aastore
	 *         13: invokevirtual #30                 // Method java/io/PrintStream.printf:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream;
	 *         16: pop
	 *         17: return
	 *       LineNumberTable:
	 *         line 37: 0
	 *       LocalVariableTable:
	 *         Start  Length  Slot  Name   Signature
	 *             0      18     0   str   Ljava/lang/String;
	 *             
	 *             
	 *             
	 * BootstrapMethods:
	 *   0: #111 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
	 *     Method arguments:
	 *       #112 (Ljava/lang/Object;)V
	 *       #113 invokestatic com/yangzl/jvm/InvokeDynamicTest.lambda$main$0:(Ljava/lang/String;)V
	 *       #114 (Ljava/lang/String;)V
	 *   1: #111 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
	 *     Method arguments:
	 *       #116 (Ljava/lang/Object;)Ljava/lang/Object;
	 *       #117 invokestatic com/yangzl/jvm/InvokeDynamicTest.lambda$main$1:(Ljava/lang/String;)Ljava/lang/Integer;
	 *       #118 (Ljava/lang/String;)Ljava/lang/Integer;
	 *   2: #111 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
	 *     Method arguments:
	 *       #112 (Ljava/lang/Object;)V
	 *       #124 invokestatic com/yangzl/jvm/InvokeDynamicTest.lambda$testLambda$2:(Ljava/lang/String;)V
	 *       #114 (Ljava/lang/String;)V
	 *   3: #111 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
	 *     Method arguments:
	 *       #116 (Ljava/lang/Object;)Ljava/lang/Object;
	 *       #125 invokestatic com/yangzl/jvm/InvokeDynamicTest.lambda$testLambda$3:(Ljava/lang/String;)Ljava/lang/Integer;
	 *       #118 (Ljava/lang/String;)Ljava/lang/Integer;
	 *   4: #111 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
	 *     Method arguments:
	 *       #126 ()Ljava/lang/Object;
	 *       #127 invokestatic com/yangzl/jvm/InvokeDynamicTest.lambda$testLambda$4:()Ljava/lang/String;
	 *       #128 ()Ljava/lang/String;
	 *   5: #111 invokestatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
	 *     Method arguments:
	 *       #130 (Ljava/lang/Object;)Z
	 *       #131 invokestatic com/yangzl/jvm/InvokeDynamicTest.lambda$testLambda$5:(Ljava/lang/String;)Z
	 *       #132 (Ljava/lang/String;)Z
	 */
	public void testLambda() {
		Consumer<String> c = str -> System.out.printf("consume: %s", str);
		Function<String, Integer> f = str -> str.length();
		Supplier<String> s = () -> "str";
		Predicate<String> p = str -> str.contains("h");
		/*
		 * public static CallSite metafactory(MethodHandles.Lookup caller,
		 *                                        String invokedName,
		 *                                        MethodType invokedType,
		 *                                        MethodType samMethodType,
		 *                                        MethodHandle implMethod,
		 *                                        MethodType instantiatedMethodType)
		 *             throws LambdaConversionException {
		 *         AbstractValidatingLambdaMetafactory mf;
		 *         mf = new InnerClassLambdaMetafactory(caller, invokedType,
		 *                                              invokedName, samMethodType,
		 *                                              implMethod, instantiatedMethodType,
		 *                                              false, EMPTY_CLASS_ARRAY, EMPTY_MT_ARRAY);
		 *         mf.validateMetafactoryArgs();
		 *         return mf.buildCallSite();
		 *     }
		 */
	}
}
