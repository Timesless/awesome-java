package com.yangzl.performance;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzl
 * @date 2020/11/29 14:10
 *
 * 编译器的优化
 * 		 fibnacci 的结果从未被使用，智能的编译器最终执行的代码只有时间的计算 0ms
 * 			实际上，将局部变量l的定义改为实例变量（并用关键字volatile声明）就能测试这个方法的性能了
 * 		这里并没有预热，等server编译器编译之后应该会优化
 * 	
 * 	============================================================================
 * 	JIT
 * 	client 编译器 c1， server编译器 c2
 * 	
 * 	因为client编译器有3种级别，所以总共有5种执行级别。因此，编译级别有：
 * 		• 0：解释代码
 * 		• 1：简单C1编译代码
 * 		• 2：受限的C1编译代码（不需要性能分析的反馈信息）
 * 		• 3：完全C1编译代码
 * 		• 4:C2编译代码
 * 	多数方法第一次编译级别从3开始，当c1编译队列满时，会执行2 -> 4，或者等待级别3的编译
 * 	c2编译队（级别4）列满了，会执行 2 -> 3 -> 4
 * 	
 * 	Java8 默认开启分层编译
 * 	
 * 	-XX:CompileThreshold=N, client N = 1500, server N = 10000
 * 	
 * 	OSR trigger = (CompileThreshold * (OnstackReplacePercentage - InterpreterProfilePercentage) / 100)
 * 	
 * 	-XX:OnStackReplacePercentage = N, client N = 933, server N = 140
 * 	
 * 	-XX:InterpreterProfilePercentage = 33
 * 	
 * 	**************那么 OSR server = 10000 * (140 -33) / 100, OSR client = 1500 * (933 -33) / 100 = 13500
 *
 * 	-XX:+PrintCompilation
 *
 * 	timestamp (compilation id) attributes (tiered level) (method name) size deopt
 * 		115    5       4       java.lang.String::charAt (29 bytes)
 * 		115   11     n 0       java.lang.System::arraycopy (native)   (static)
 * 		121   22       3       com.yangzl.performance.CompilerOptimization::fib (37 bytes)   made not entrant
 * 		...	
 * 		10149  103 %     3       com.yangzl.performance.CompilerOptimization::doFib @ 7 (57 bytes)	
 *
 * 	attributes:
 * 		• %：编译为OSR（On-Stack Replacement, 栈上替换）编译整个循环而不是方法，而且在循环运行时编译，当次循环结束后替换还在栈上的代码
 * 		• s：方法是同步的。
 * 		• ! ：方法有异常处理器。
 * 		• b：阻塞模式时发生的编译。
 * 		• n：为封装本地方法所发生的编译。
 * 	
 * 	deopt 标明发生了某种逆优化，通常是 “made not entrant，代码被丢弃” “made zombie”
 * 	
 * 	请注意，OSR编译过的构造函数和标准编译过的方法都被标记成made not entrant，过了一会，它们又被标记成madezombie。
 * 	
 * 	=======================================================================================
 *
 *	内联
 *	-XX:+Inline
 * 	-XX:+PrintInlining
 *
 * 	热点方法 字节码 < 325字节时内联，普通方法字节码 < 35 字节时内联
 * 	-XX:MaxInlineSize=400
 * 	
 * 		Point p = getPoint()
 * 		p.setX(p.getX() << 1)
 * 	编译后实际为：
 * 		p.x = p.x << 1;
 * 	
 * 	
 * 	=============================================================================
 * 	逃逸分析
 * 	-XX:+DoEscapeAnalysis
 * 	server 编译器将执行一些非常激进的优化措施
 * 		如下 factorial栗子
 * 		1. op只在循环中引用，没有任何其它代码访问此对象，因此server编译器会去除同步
 * 			对OS来说：op对象没必要在内存中保存，可以在寄存器中保存
 * 			对应JVM来说：op对象没必要在堆中保存，在栈中保存就行了	
 * 		2. 事实上，根本不需要分配对象，只需要这个对象的个别字段
 * 	
 */
public class CompilerOptimization {
	
	private double answer;
	
	
	private volatile ConcurrentHashMap<String, Object> instance;
	/**
	 * 2020/11/29 这里完成一个标准的双重检测锁
	 * 			volatile 主要是防止外层的循环，访问到指令重排之后的instance
	 * 			
	 * 其一，可以注意到，HashMap是先用一个局部变量初始化的，而且只有完全初始化的最终值才会被赋值给instance变量
	 *
	 */
	public void doOperation() {
		ConcurrentHashMap<String, Object> ins = this.instance;
		if (null == ins) {
			synchronized (this) {
				ins = this.instance;
				if (null == ins) {
					ins = new ConcurrentHashMap<>(16);
					// ins.put...
					instance = ins;
				}
			}
		}
		
		// use the instance
	}
	
	
	/**
	 *
	 */
	public void doFib() {
		double l;
		long t1 = System.currentTimeMillis();
		for (long i = 0; i < 20000; ++i) {
			l = fib(24);
		}
		System.out.printf("fibnacci 50 执行10000次耗时：%d \n", System.currentTimeMillis() - t1);
	}
	private double fib(int N) {
		if (N <= 0) {
			throw new IllegalArgumentException("must be > 0");
		}
		if (N <= 2) {
			return 1;
		}
		return fib(N - 1) + fib(N - 2);
	}
	
	public synchronized long factorial(int n) {
		if (n <= 0) {
			return 1;
		}
		if (n <= 2) {
			return n;
		}
		return n * factorial(n - 1);
	}
	
	
	/**
	 * 2020/11/29 op只在循环中引用，没有任何其它代码访问此对象，因此server编译器会去除同步
	 * 
	 */
	@Test
	public void testFactorial() {
		List<Long> list = new ArrayList<>(20);
		for (int i = 0; i < 20; ++i) {
			CompilerOptimization op = new CompilerOptimization();
			long rs = op.factorial(15);
			list.add(rs);
		}
		System.out.println(list);
	}


	/**
	 * 2020/11/29
	 * 
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		CompilerOptimization client = new CompilerOptimization();
		client.doFib();
		client.doFib();
		client.doFib();
		try { TimeUnit.SECONDS.sleep(4); } catch(InterruptedException e) { e.printStackTrace(); }
		client.doFib();
		client.doFib();
		try { TimeUnit.SECONDS.sleep(4); } catch(InterruptedException e) { e.printStackTrace(); }
		client.doFib();
	}
	
	
	public void computeAnswer() {
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 10_000; ++i) {
			answer = compute(i);
		}
		System.out.printf("花费时间：%d", System.currentTimeMillis() - t1);
	}
	private double compute(int i) {
		return 0.0d;
	}
	
	/**
	 * 2020/11/29 answer的值只会保留最后一次计算的，所以可能会优化为将 answer放在寄存器中，而不是内存
	 * 
	 * 		编译器可能优化代码，展开循环，得到如下伪代码
	 * 	============================== 为什么不优化到缓存行大小 64K 呢，或者说这里是通用寄存器的数量？？	
	 * 			for (int i = 0; i < 100; i += 4) {
	 * 				answer = compute(i);
	 * 				answer = compute(i + 1);
	 * 				answer = compute(i + 2);
	 * 				answer = compute(i + 3);	
	 * 			}
	 * 		如果JVM把answer的值保存在寄存器中，寄存器被写了多次，但没有读取操作（其它线程无法读取该寄存器），
	 * 		因此除了最终结果，可以优化掉所有循环计算
	 * 		
	 * 		如果要解决这个问题，请使用volatile关键字定义answer，这样确保每次寄存器修改值后会刷回主存
	 * 	
	 * 	==================== 寄存器值刷回主存的时机，就由线程同步控制
	 * 	
	 * 		JVM无法优化调这些计算，因为JVM不知道是否有其他线程读取主存（main memory）中的值	
	 * 			private volatile double answer;	
	 * 		
	 * 		所以双重检测锁的单例，需要volatile关键字定义	
	 * 
	 */
	@Test
	public void testOptimization() {
		computeAnswer();
	}

}
