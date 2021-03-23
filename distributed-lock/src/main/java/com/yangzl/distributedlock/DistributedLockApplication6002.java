package com.yangzl.distributedlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yangzl
 * @date 2021/3/22
 * @desc 节点2，server.port = 6002
 */
@SpringBootApplication
public class DistributedLockApplication6002 {

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "dev2");
		SpringApplication.run(DistributedLockApplication6002.class, args);
	}

}
