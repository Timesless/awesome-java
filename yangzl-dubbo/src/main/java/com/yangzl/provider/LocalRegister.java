package com.yangzl.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangzl
 * @date 2021/3/29
 *
 * 本地服务注册表
 */
public class LocalRegister {

	private static final Map<String, Class<?>> services = new HashMap<>(64);

	public static void regist(String interfaceName, Class<?> implClass) {
		services.put(interfaceName, implClass);
	}

	public static Class<?> get(String interfaceName) {
		return services.get(interfaceName);
	}
}
