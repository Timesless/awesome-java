package com.yangzl;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
    
    
    @Test
	public void testDraw() {
		int N = 50;
		double[] arr = new double[N];
		for (int i = 0; i < N; i++) {
			arr[i] = StdRandom.uniform();
		}
		Arrays.sort(arr);
		StdDraw.setPenColor(StdDraw.GRAY);
		for (int i = 0; i < N; i++) {
			double x = 1.0 * i / N;
			double y = arr[i] / 2.0;
			double rw = 0.5 / N;
			double rh = arr[i] / 2.0;
			StdDraw.filledRectangle(x, y, rw, rh);
		}
		try { TimeUnit.SECONDS.sleep(0X7FFFFFFF); } catch(InterruptedException e) { e.printStackTrace(); }
	}
}
