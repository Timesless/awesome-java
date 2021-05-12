package com.yangzl.spring;

import com.yangzl.spring.annotation.*;
import com.yangzl.spring.aware.BeanNameAware;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author yangzl
 * @date 2021/3/28
 *
 * 模拟 spring ApplicationContext
 */
public class ApplicationContext {
	private static final String PROTOTYPE = "prototype";
	private static final String SINGLETON = "singleton";

	/**
	 * beandefinition map
	 */
	private final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(64);
	/**
	 * singleton objects pool
	 */
	private final ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>(64);
	/**
	 * postprocessors
	 */
	private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>(16);

	public ApplicationContext(Class<?> appConfig) {
		refresh(appConfig);
	}

	/**
	 * 1. scan packages
	 * 2. beandefinitions
	 * 3. createBean
	 *
	 * @param appConfig bootConfig
	 */
	private void refresh(Class<?> appConfig) {

		doComponentScan(appConfig);
		createBeansByBeanDefinitions();

	}


	public Object getBean(String beanName) {
		BeanDefinition bd = beanDefinitionMap.get(beanName);
		if (PROTOTYPE.equals(bd.getScope())) {
			return doCreateBean(bd);
		}
		Object singleton = singletonObjects.get(beanName);
		Object rs = null == singleton ? doCreateBean(bd) : singleton;
		singletonObjects.put(beanName, rs);
		return rs;
	}

	/**
	 * if is singleton && non lazy bean
	 * 1. create bean
	 * 2. initialization
	 * 3. populateProperty
	 * 4. Aware
	 * 5. singletonObjects
	 */
	private void createBeansByBeanDefinitions() {

		beanDefinitionMap.forEach((k, v) -> {
			if (PROTOTYPE.equals(v.getScope()) || Boolean.TRUE.equals(v.getLazy())) {
				;
			} else {
				// singleton and non-lazyinit bean will initialization
				Object bean = doCreateBean(v);
				singletonObjects.put(k, bean);
			}
		});
	}

	/**
	 * create bean
	 * 		0. awares
	 * 		1. afterPropertySet
	 * 		2. BeanPostProcessor#before
	 * 		3. BeanPostProcessor#after
	 *
	 * @param beanDefinition bd
	 * @return object
	 */
	private Object doCreateBean(BeanDefinition beanDefinition) {
		Object bean = null;
		try {
			/*
			 * TODO 这里的顺序是否应该调整
			 */
			Class<?> clazz = beanDefinition.getBeanClass();
			bean = clazz.newInstance();
			String beanName = beanDefinition.getBeanClassName();
			autowireFields(bean, clazz);

			if (bean instanceof BeanNameAware) {
				((BeanNameAware) bean).setBeanName(beanName);
			}
			// 初始化前可以调用 BeanPostProcessor#before
			for (BeanPostProcessor bp : beanPostProcessorList) {
				bean = bp.postProcessorBeforeInitialization(bean, beanName);
			}
			// 初始化
			if (bean instanceof InitializingBean) {
				((InitializingBean) bean).afterPropertySet();
			}
			// 初始化后可以调用 BeanPostProcessor#after
			for (BeanPostProcessor bp : beanPostProcessorList) {
				bean = bp.postProcessorAfterInitialization(bean, beanName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 自动注入属性 @Autowired 字段赋值
	 *
	 * @param bean cur bean
	 * @param clazz cur class
	 * @throws IllegalAccessException ex
	 */
	private void autowireFields(Object bean, Class<?> clazz) throws IllegalAccessException {
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(Autowired.class)) {
				f.setAccessible(true);
				String fName = f.getName();
				// field name to get field bd
				String captialFName = String.valueOf(fName.charAt(0)).toUpperCase() + fName.substring(1);
				Object fieldObj = getBean(captialFName);
				f.set(bean, fieldObj);
			}
		}
	}

	/**
	 * scan packages and generate beandefinition
	 */
	public void doComponentScan(Class<?> bootConfig) {
		ComponentScan scan = bootConfig.getAnnotation(ComponentScan.class);
		ClassLoader threadLoader = ApplicationContext.class.getClassLoader();
		URL byteResource = threadLoader.getResource(scan.value().replace(".", "/"));
		Stream<Path> paths = null;
		try {
			Path scanPath = Paths.get(byteResource.toURI());
			paths = Files.walk(scanPath).filter(Files::isRegularFile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		paths.forEach(path -> {
			String fileName = path.getFileName().toString(), abslutePath = path.toAbsolutePath().toString();
			String beanName = fileName.substring(0, fileName.indexOf("."));
			String fullName = abslutePath.substring(abslutePath.indexOf("com"), abslutePath.indexOf(".class"));
			String fullQualifiedName = fullName.replace("\\", ".");
			try {
				Class<?> clazz = threadLoader.loadClass(fullQualifiedName);
				if (clazz.isAnnotationPresent(Component.class)) {
					BeanDefinition bd = new BeanDefinition();
					bd.setBeanClassName(beanName);
					bd.setBeanClass(clazz);
					Scope scope = clazz.getAnnotation(Scope.class);
					if (null != scope) {
						bd.setScope(scope.value());
					} else {
						bd.setScope(SINGLETON);
					}
					Lazy lazy = clazz.getAnnotation(Lazy.class);
					if (null != lazy) {
						bd.setLazy(lazy.value());
					}
					// put into beandefinitionMap
					beanDefinitionMap.put(beanName, bd);

					// put into postprocessorList
					if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
						BeanPostProcessor p = (BeanPostProcessor) clazz.newInstance();
						beanPostProcessorList.add(p);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}


	// =================================================================================
	// divide
	// =================================================================================


	public static void main(String[] args) throws IOException {
		/*Files.walk(Paths.get("d:/wsl")).filter(Files::isRegularFile).forEach(path -> {
			System.out.println(path.getFileName());
		});*/
		System.out.println("1".equals(null));
	}

}
