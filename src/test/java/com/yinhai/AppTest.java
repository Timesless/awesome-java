package com.yinhai;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    /*
     * 字节数组
     */
    @Test
    public void test1() {
        String str = "i like boom boom boom";
        byte[] bytes = str.getBytes();
        System.out.println(Arrays.toString(bytes));
        byte b = -88;
        int s = b;
        System.out.println(Integer.toBinaryString(s));
        System.out.println(Integer.toBinaryString(32));
    }
    /**
     * @Date: 2020/1/4
     * @Desc: java.util.function
     **/
    @Test
    public void testFunctionalInterface() {

        Function<String, Integer> f = str -> str.length();
        Function<Integer, String> f2 = num -> String.valueOf(num);
        System.out.println(f2.andThen(f).apply(1234));
        Stream<String> stream = Stream.of("i", "love", "this");
        System.out.println(stream.collect(Collectors.toMap(Function.identity(),
                String::length)));
        String str = "123";
        Predicate<String> predicate = Predicate.isEqual(str);
        System.out.println(predicate.test("123"));
    }
    @Test
    public void testIntStream() {
        System.out.println(IntStream.rangeClosed(1, 1000_00).sum());
    }
    
}
