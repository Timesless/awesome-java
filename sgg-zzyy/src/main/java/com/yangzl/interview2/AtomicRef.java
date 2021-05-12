package com.yangzl.interview2;

import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author yangzl
 * @date 2021/3/19
 *
 * 		原子引用更新 AtomicReference<V>, AtomicStampedReference<V>
 * 		 AtomicStampedReference 可以解决 ABA 问题
 */
@Slf4j
public class AtomicRef {

	@ToString
	@Setter
	static class User {
		private String name;
		User(String name) {
			this.name = name;
		}
	}

	/**
	 * AtomicReference
	 */
	@Test
	public void testAtomicReference() {
		User zs = new User("zs");
		User ls = new User("ls");
		AtomicReference<User> atomic = new AtomicReference<>(zs);
		atomic.compareAndSet(zs, ls);

		log.info("current user = {}", atomic.get());
	}

	/**
	 * AtomicStampedReference
	 */
	@Test
	public void testAtomicStampedReference() {
		int initVersion = 1;
		AtomicStampedReference<Integer> ref = new AtomicStampedReference<>(1, initVersion);
		ref.compareAndSet(1, 2, initVersion, initVersion + 1);
	}

	@Test
	public void testResolveABA() {

	}

}
