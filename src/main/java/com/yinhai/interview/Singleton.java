package com.yinhai.interview;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author: yangzl
 * @Date: 2019/10/6 14:58
 * @Desc:	带属性的单列，读取配置文件属性
 **/
public class Singleton {

	private String info;
	public static final Singleton INSTANCE;
	static {
		Properties prop = new Properties();
		try {
			prop.load(Singleton.class.getClassLoader().getResourceAsStream("interview/singleton.propertites"));
			INSTANCE = new Singleton(prop.getProperty("info"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	private Singleton(String _info) {
		this.info = _info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
}
