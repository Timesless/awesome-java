package com.yangzl;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void shouldAnswerWithTrue() {
        assertTrue( true );
    }


	@Test
	public void testFix() {
		List<String> strs = new ArrayList<>(4);
		strs.add("helllo");
		strs.add("wwoo");
		for (int i = 0; i < strs.size(); ++i) {
			String str = strs.get(i);
			Pattern patT = Pattern.compile("([0-9a-zA-Z])\\1{2}");
			Matcher matcher = patT.matcher(str);
			while (matcher.find()) {
				System.out.println(matcher.group());
				System.out.println(matcher.group(1));
				System.out.println(str.replaceAll("([0-9a-zA-Z])\\1{2}", "$1"));
			}
			Pattern p2 = Pattern.compile("(\\w)\\1(\\w)\\2");
			Matcher m2 = p2.matcher(str);
			while (m2.find()) {
				String g = m2.group();
				String g1 = m2.group(1);
				String g2 = m2.group(2);
				System.out.println(g);
				System.out.println(g1);
				System.out.println(g2);
				System.out.println(str.replaceAll("(\\w)\\1(\\w)\\2", g1 + g1 + g2));
			}
		}
	}
	
	
	@Test
	public void testReplace() {
    	String str = "helllowwoohhzzz";
		System.out.println(str
				.replaceAll("([0-9a-zA-Z])\\1{2}", "$1$1")
				.replaceAll("(\\w)\\1(\\w)\\2", "$1$1$2"));
		
		String str2 = "wwoohh";
		System.out.println(str2.replaceAll("(\\w)\\1(\\w)\\2", "$1$1$2"));
	}
	
	
}
