package com.yangzl.volume;

import org.junit.jupiter.api.Test;

/**
 * @Author yangzl
 * @Date: 2020/5/26 19:54
 * @Desc: 使用IEEE严格浮点运算模式，标记方法或类
 *  整数被0除将会产生一个异常，而浮点数被0除将会得到无穷大或NaN
 *  StrictMath确保在所有平台运算得到相同结果
 */
public strictfp class Day1 {

    private String name;

    public void sayName() {
        String otherName = "";
        System.out.println(name);
        System.out.println(otherName);
    }

    @Test
    public void test1() {

        /**
         * 码点和代码单元
         *  码点：Unicode为字符分配的编号，一个字符只占一个码点
         *  代码单元：针对编码方式而言，指编码方式中对一个字符编码以后占的最小存储单元
         *  例如UTF-8，代码单元是1B，因为一个字符可被编码为1B,2B,3B,4B；在UTF-16中代码单元
         *  变成2B（char），因为一个字符可被编码为1个 或 2个char
         *      一个字符对应一个码点，可能有多个代码单元
         *
         *  在使用String的length和charAt时，返回的时代码单元的数量，及指定位置的代码单元，而不是码点
         */
        String s = "\uD869\uDEA5";
        System.out.println(s.length());

        // exit hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("bye bye")));
    }

    @Test
    public void testContinue() {
        outer:
        for (int i = 0; i < 3; ++i) {
            inner:
            for (int j = 1; j < 5; ++j) {
                if (i == 1) {  break inner; }
                System.out.printf("i = %d, j = %d\n", i, j);
            }
        }
    }

    /**
     * 2020/6/14 8个基本类型包装类，及String常量池测试
     * @param
     * @return
     */
    @Test
    public void testConstantPool() {
        Long l1 = 100L, l2 = 100L;

        int i1 = 100, i2 = 100;

        String s1 = "hh";
        String s = "hh";
        s1.intern();
        System.out.println(s1 == s);
    }

    @Test
    public void testRemoveSync() {
        synchronized (new Object()) {
            System.out.println("...");
        }
    }

}
