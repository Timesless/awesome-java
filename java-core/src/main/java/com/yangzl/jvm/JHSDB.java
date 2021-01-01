package com.yangzl.jvm;

/**
 * @author yangzl
 * @date 2020/11/5 09:47
 * @desc JHSDB 启动请配置容量不然很卡
 * 		-Xmx10m -XX:+UseSerialGC -XX:-UseCompressedOops
 * 	由于jhsdb在JDK9才提供，JDK8及之前只能使用sa-jdi.jar启动 java -cp sa-jdi.jar sun.jvm.hotspot.HSDB
 * 	jhsdb hsdb --pid vmid
 * 
 * 	1. 查看虚方法表「vtable」、接口方法表「itable」
 * 		attach to hotspot process
 * 		tools => class browser
 * 		tools => inspector 输入内存地址
 * 		可以看到vtable长度，其中5个是从Object继承的
 * 		public
 * 			String toString()
 * 			void finalize()
 * 			native int hashCode()
 * 			boolean equals(java.lang.Object)	
 * 			native java.lang.Object clone()
 * 		
 * 	2. 查看 staticObj instanceObj localObj在堆中的布局
 * 		staticObj引用「指针」随类型信息存放在「方法区」
 * 		instanceObj引用「指针」存放在堆
 * 		localObject引用「指针」存放在sayHello帧的「局部变量表」
 * 	 Tools => Object Histogram => com.yangzl.jvm.JHSDB$Data => inspect
 *   打开console => revptrs 对象地址	
 */
public class JHSDB {
	
	private static class Data {}

	static abstract class AHolder {
		public void print() {
			System.out.println("I Love Vim");
		}
		/** 虚方法表 vtable */
		public abstract void sayHello();
	}

	static class BHodler extends AHolder {
		/** staticObj 布局 */
		static Data staticObj = new Data();
		/** instanceObj 布局 */
		Data instanceObj = new Data();
		@Override
		public void sayHello() {
			Data localObj = new Data();
			// TODO 请在此处断点
			System.out.println("done");
		}
	}
	
	public static void main(String[] args) {
		AHolder obj = new BHodler();
		obj.sayHello();
	}
}


