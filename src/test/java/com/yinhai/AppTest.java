package com.yinhai;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
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
    
}
