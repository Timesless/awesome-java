package com.yangzl.jvm.instruction;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author yangzl
 * @date 2020/12/30 19:13
 *
 * 字节码角度解析
 * 			try - catch - finally 
 * 			try - with - resources
 */
public class TryCatchTest {
	
	private void handle() {}
	private void handleFinally() {}
	private void handleException() {}
	
	
	public void sugarTcf() {
		try {
			handle();
		} catch (RuntimeException r) {
			handleException();
		} finally {
			handleFinally();
		}
	}
	/**
	 * try - catch - finally desugar
	 * finally出现了3次，Java编译器复制finally块并将其内容插入到try 和 catch块所有正常 / 异常退出之前
	 * 
	 */
	public void trulyTcf() {
		try {
			handle();
			handleFinally();
		} catch (RuntimeException r) {
			try {
				handleException();
			} catch (Throwable t) {
				handleFinally();
				// 这是必要的，catch未捕获到的需要往外抛
				throw t;
			}
		} catch (Throwable t) {
			handleFinally();
			// finally异常也需要往外抛
			throw t;
		}
	}
	
	/**
	 * 字节码理解
	 * tips：发生异常时JVM将异常自动压至栈顶
	 * 
	 * astore_1「r -> 局部变量表」
	 * iconst_1
	 * iconst_0
	 * idiv
	 * istore_2「a = 1 / 0」
	 * iconst_1
	 * istore_3「将要返回的1 -> tmp，这个返回没有机会执行」
	 * iconst2
	 * ireturn
	 */
	public int foo() {
		try {
			int a = 1 / 0;
			return 0;
		} catch (RuntimeException r) {
			int b = 1 / 0;
			return 1;
		} finally {
			return 2;
		}
	}
	
	/**
	 * bipush	100
	 * istore_1
	 * iload_1
	 * istore_2「100 -> tmp」
	 * iinc		1, 1 「tips：iinc直接在局部变量表中自增，无需加载到操作数栈中再保存回局部变量表」
	 * iload_2
	 * ireturn
	 * astore_3
	 * iinc		1, 1
	 * aload_3
	 * athrow
	 * 
	 * @return int
	 */
	public int sugarBar() {
		int i = 100;
		try {
			return i;
		} finally {
			++ i;
		}
	}
	public int trulyBar() {
		int i = 100;
		try {
			int tmp = i;
			++ i;
			return tmp;
		} catch (Throwable t) {
			++ i;
			// finally中发生异常
			throw t;
		}
	}
	
	
	// ==================================================================
	// try - with - resources
	// ==================================================================
	
	/**
	 * 根据Java编译器对finally的处理，finally的语句将被复制到try 与 catch块所有正常 / 异常返回之前
	 * 所以这里得到的是finally exception
	 * 也就是说：当try和finally都抛出异常时，finally 会覆盖掉 catch 的 exception，try中异常莫名的丢了
	 * try - with - resources 可以解决异常丢失的情况
	 * 
	 * @return void
	 */
	public static void rfoo() {
		try {
			throw new RuntimeException("catch exception");
		} finally {
			throw new RuntimeException("finally exception");
		}
	}
	
	
	public void sugarTryWithResources() throws IOException {
		try (FileInputStream fis = new FileInputStream("d:/tmp")) {
			fis.read();
		}
	}
	/**
	 * try-with-resources语法糖
	 * 在catch中捕获到try中发生的异常 tmpException
	 * 在finally如果同样有异常，那么调用 tmpException.addSuppressed(finallyException)
	 * 这样try中异常不会被覆盖，这样既可以抛出真正异常「下文中e」还可以附带finally中异常
	 * 
	 * @return void
	 */
	public void trulyTryWithResources() throws IOException {
		FileInputStream fis = null;
		Exception tmpException = null;
		try {
			fis = new FileInputStream("d:/tmp");
			fis.read();
		} catch (Exception e) {
			tmpException = e;
			// 最后会走到这里来抛出e
			throw e;
		} finally {
			if (fis != null) {
				if (tmpException != null) {
					try {
						fis.close();
					} catch (Exception finallyException) {
						tmpException.addSuppressed(finallyException);
					}
				} else {
					fis.close();
				}
			}
		}
	}

}
