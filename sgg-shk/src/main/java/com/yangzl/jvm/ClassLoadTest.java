package com.yangzl.jvm;

import sun.misc.Launcher;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzl
 * @date 2021/3/12
 *
 * JVM 类加载
 *
 * 所有被 JVM 识别的字节码文件都以魔数「0xcafebabe」开头
 * 主要包含 4 种验证：文件格式、元数据、字节码、符号引用
 *
 * + 验证
 * 所有被 JVM 识别的字节码文件都以魔数「0xcafebabe」开头
 * 主要包含 4 种验证：文件格式、元数据、字节码、符号引用
 * + 准备
 * Prepare： 为类变量分配内存，并初始化0值；对final的静态字面量显式赋值，如果不是字面值静态常量，那么初始化0值
 * 类变量会分配在方法区
 * + 解析
 * Resolve：将一部分符号引用转换为直接引用「方法区的入口地址」
 * 前提是在编译时能确定的方法，这些方法的调用称为解析，Java中符合编译可知，运行期不变的主要包括：静态方法、私有方法、实例构造器、父类方法
 *
 * Initialization：执行 `<clinit>()`
 * `clinit<>()` 方法是 javac 编译器收集类中所有类变量赋值和静态块合并而来的
 * 如果类中无类变量和静态块，那么 javac 不会生成 `clinit<>()`
 * 若该类存在父类，那么 jvm 保证父类的 `clinit<>()` 会先执行
 * jvm 保证一个类的 `clinit<>()` 在多线程下被同步加锁，且只加载一次
 *
 * 		类加载器：
 * 			AppClassLoader：系统类加载器 / 应用类加载器
 * 			ExtClassLoader：扩展类加载器
 * 			BootStrapClassLoader：根加载器
 *
 */
public class ClassLoadTest {

	/*
	ClassLoader API
		getParent()
		loadClass(String name)
		findClass(String)
		findLoadedClass(String)
		defineClass(String, byte[], int off, int len)
		resolveClass<Class<?>>
	 */

	/**
	 * 重写 findClass
	 */
	static class CustomClassLoader extends ClassLoader {
		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			byte[] bytes = getCustomClassBytes();
			return defineClass(name, bytes, 0, bytes.length);
		}
		// 获取自定义类字节码的二进制字节流
		private byte[] getCustomClassBytes() {
			return new byte[1024];
		}
	}

	static class UserClassLoader extends URLClassLoader {
		public UserClassLoader(URL[] urls) {
			super(urls);
		}
		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			return super.findClass(name);
		}
	}

	/**
	 * 静态内部类，只是写在其它类的内部，除此之外与外部类无异
	 */
	static class Load {
		static {
			System.out.println(Thread.currentThread().getName() + "load class");
			try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
		}
	}

	/**
	 * 类加载测试
	 */
	private static void testClassLoad() {
		Runnable r = () -> {
			new Load();
		};
		new Thread(r, "T A").start();
		new Thread(r, "T B").start();
		new Thread(r, "T C").start();
	}

	/**
	 * 类加载器测试
	 */
	private static void testClassLoader() {
		ClassLoader appLoader = ClassLoader.getSystemClassLoader();
		System.out.println(appLoader);
		ClassLoader extLoader = appLoader.getParent();
		System.out.println(extLoader);
		ClassLoader bootStrapLoader = extLoader.getParent();
		System.out.println(bootStrapLoader);

		System.out.println("=====================================");
		// 获取 bootstrap 加载哪些库
		URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
		for (URL urL : urLs) {
			System.out.println(urL);
		}

		// 获取 ExtClassLoader 加载哪些库
		String extDirs = System.getProperty("java.ext.dirs");
		System.out.println(Arrays.toString(extDirs.split(";")));
	}

	public static void main(String[] args) {
		testClassLoader();
		testClassLoad();
	}
}
