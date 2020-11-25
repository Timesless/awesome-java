package com.yangzl.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author yangzl
 * @date 2020/11/17 20:54
 * @desc 文件工具类
 */

public class FileUtils {
	
	/**
	 * 2020/11/17 从指定目录下查找文件
	 * 
	 * @param fileName 要查找的文件名
	 * @param target target dir
	 * @return List
	 */
	public List<String> findFile(String fileName, String target) {
		List<String> rs = new ArrayList<>();
		try (Stream<Path> files = Files.walk(Paths.get(target))) {
			files.forEach(cur -> {
				if (cur.getFileName().toString().contains(fileName)) {
					rs.add(cur.toAbsolutePath().toString());
				}
			});
		} catch (IOException e) {
			System.out.println("目录不存在...");
			e.printStackTrace();
		}
		return rs;
	}
	
	@Test
	public void testFindFile() {
		List<String> files = findFile("PollSelectorImpl.java",
				"G:/chrome download/jdk-jdk8-b120/jdk/src/");
		for (String tmp : files) {
			System.out.println(tmp);
		}
	}

}
