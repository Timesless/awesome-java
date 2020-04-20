package com.yangzl;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AppTest {
    /**
     * Rigorous Test :-)
     */
    /**
     * @Date: 2020/2/12
     * @Desc: 正确理解泛型通配符，协变，逆变
     **/
    @Test
    public void test3() {
        /*
         * extends 确定了泛型上界
         * super 确定泛型下界
         */
        /*
         * ? extends Number中，?表示Number的子类，例如Integer
         * 该通配符实现协变，则 ArrayList<Integer>是ArrayList<Number>的子类
         * 父类能出现的地方，子类一定可以透明的引用（即可以使用Integer）
         */
        ArrayList<? extends Number> list1 = new ArrayList<Integer>();
        /*
         * ? super Number，? 表示Number的父类，如Object
         * 该通配符实现逆变，则ArrayList<Object>是ArrayList<Number>的子类
         * 父类能出现的地方，子类一定可以透明的引用（即可以使用Object）
         */
        ArrayList<? super Number> list2 = new ArrayList<Object>();
    }
    
    
    @Test
    public void shouldAnswerWithTrue() {
        int[] test =  {1, 5};
        test[1]++;
        System.out.println(Arrays.toString(test));
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
        Function<String, Integer> f = String::length;
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
    
    /**
     * @Date: 2020/2/6
     * @Desc:  x++(fast) ++x x=x+1
     **/
    @Test
    public void test2() {
        for (int m = 0; m < 10; ++m) {
            int x = 0, y = 0, z = 0;
            long s1 = System.currentTimeMillis();
            for (long i = 0; i < 5000_000_0L; ++i) { x++; }
            System.out.print(System.currentTimeMillis() - s1 + " ");

            long s2 = System.currentTimeMillis();
            for (long i = 0; i < 5000_000_0L; ++i) { ++y; }
            System.out.print(System.currentTimeMillis() - s2 + " ");

            long s3 = System.currentTimeMillis();
            for (long i = 0; i < 5000_000_0L; ++i) { z = (z + 1); }
            System.out.println(System.currentTimeMillis() - s3);
        }
    }

    public int[] smallerNumbersThanCurrent(int[] copy) {
        int n = copy.length;
        int[] nums = Arrays.copyOf(copy, n);
        Arrays.sort(nums);
        Map<Integer, Integer> map = new HashMap<>(n << 1);
        int[] tmp = new int[n];
        for (int i = 1; i < n; ++i) {
            if (nums[i] == nums[i - 1])
                tmp[i] = tmp[i - 1];
            else
                tmp[i] = i;
            map.put(nums[i], tmp[i]);
        }
        map.put(nums[0], 0);
        int[] rs = new int[n];
        for(int i = 0; i < n; ++i)
            rs[i] = map.get(copy[i]);
        return rs;
    }
    @Test
    public void testSmallerNumbersThanCurrent() {
        int[] param = {8, 1, 2, 2, 3};
        System.out.println(Arrays.toString(smallerNumbersThanCurrent(param)));
    }

    /**
     * @Date: 2020/3/22
     * @Desc:  TIMEOUT
     */
    public int minIncrementForUnique(int[] A) {
        if (A.length == 0 || A.length == 1) return 0;
        Set<Integer> set = Arrays.stream(A)
                .mapToObj(Integer::valueOf).collect(Collectors.toSet());
        int max = Arrays.stream(A).max().getAsInt();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < A.length; ++i)
            map.merge(A[i], 1, Integer::sum);
        List<Map.Entry<Integer, Integer>> list = map.entrySet().stream()
                .filter(node -> node.getValue() > 1)
                .collect(Collectors.toList());
        Iterator<Map.Entry<Integer, Integer>> iter = list.iterator();
        int count = 0;
        while (iter.hasNext()) {
            Map.Entry<Integer, Integer> node = iter.next();
            int k = node.getKey();
            int v = node.getValue();
            while (v > 1) {
                for (int i = k; i <= max + 1; ++i) {
                    if (!set.contains(i)) {
                        set.add(i);
                        break;
                    }
                    ++count;
                }
                --v;
                max = set.stream().max(Integer::compare).get();
            }
        }
        return count;
    }
    @Test
    public void testMinIncrementForUnique() {
        int[] arr = {2, 2, 2, 2, 0};
        System.out.println(minIncrementForUnique(arr));
    }
    
}
