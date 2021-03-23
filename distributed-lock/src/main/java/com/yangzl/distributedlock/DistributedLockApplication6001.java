package com.yangzl.distributedlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yangzl
 * @date 2021/3/22
 * @desc 节点1 server.port = 6001
 * nginx 包含配置 d:/yangzl/nginx/*.conf
 * localhost:2048/rdlock/bus/1001
 */
@SpringBootApplication
public class DistributedLockApplication6001 {

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "dev1");
		SpringApplication.run(DistributedLockApplication6001.class, args);
	}

}
