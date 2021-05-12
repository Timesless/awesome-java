package com.yangzl.jvm;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * @author yangzl
 * @date 2021/3/14
 *
 * 测试 Java8 元空间类 OOM
 *
 * -XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m
 */
public class ClassWriterTest extends ClassLoader {

	/**
	 * 大约在 3331 个类加载后，抛出 OutOfMemoryError: Compressed class space
	 *
	 * @param args 启动参数
	 */
	public static void main(String[] args) {

		int count = 0;
		try {
			ClassWriterTest classWriterTest = new ClassWriterTest();
			for (int i = 0; i < 5000; i++) {
				String className = "Class" + i;
				ClassWriter classWriter = new ClassWriter(0);
				classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null);
				byte[] classBytes = classWriter.toByteArray();

				classWriterTest.defineClass(className, classBytes, 0, classBytes.length);
				++ count;
			}
		} finally {
			System.out.println("已额外加载 " + count + " 类");
		}
	}
}
