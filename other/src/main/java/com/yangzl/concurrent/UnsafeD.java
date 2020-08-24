package com.concurrent;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @Author yangzl
 * @Date: 2020/6/7 16:30
 * @Desc:
 */
public class UnsafeD {
    int f2;
    long item;


    public static void main(String[] args) throws Exception {
        // 1
        Constructor constructor = Unsafe.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Unsafe unsafe = (Unsafe) constructor.newInstance();
        long item = unsafe.objectFieldOffset(UnsafeD.class.getDeclaredField("f2"));
        System.out.println(item);

        // 2 这种方式不会新创建
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe UNSAFE = (Unsafe) theUnsafe.get(null);
        long f2 = UNSAFE.objectFieldOffset(UnsafeD.class.getDeclaredField("f2"));
        System.out.println(f2);
    }
}
