package com.yangzl.distributedlock.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzl
 * @date 2021/3/22
 *
 * controller 商品购买
 */
@Slf4j
@RestController
public class RedisController {
	/** goods prefix */
	private static final String GOODS_PREFIX = "goods:";
	/** redis lock k */
	private static final String REDIS_GOODS_LOCK = "redisGoodsLock";
	/** 释放锁lua脚本 */
	private static final String RELEASE_LOCK_LUA_SCRIPT =
			"if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

	@Resource
	private Redisson redisson;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Value("${server.port}")
	private String port;


	/**
	 * 版本 1：单机锁
	 * 1. synchronized 与 Lock 如何选择？
	 * tryLock
	 * 公平锁
	 * 准确 signal()
	 * 单机锁，在分布式环境下会存在问题
	 *
	 * @param pid goodsId
	 * @return str
	 */
	@GetMapping("/buy1/{pid}")
	public String buyGoods1(@PathVariable String pid) {
		synchronized (this) {
			return decrGoods(pid);
		}
	}

	/**
	 * version2：添加 redis 分布式锁锁
	 *
	 * finally 无法确保被执行
	 * 1. System.exit(0)
	 * 2. 宕机
	 * 添加自动过期时间 10s
	 *
	 * Q：线程处理业务时间超过 10s ，那第二个线程进入后， 第一个线程在 12s 执行完毕，则会解锁掉第二个线程的锁
	 * 判断是自己的锁才删除
	 *
	 * Q：此处，获取锁 val 和 删除 不是原子性的，如何保证原子性？
	 * 1. 事务
	 * 2. Lua 脚本
	 *
	 * @param pid goodsId
	 * @return str
	 */
	@GetMapping("/buy2/{pid}")
	public String buyGoods2(@PathVariable String pid) {
		String result;
		String lockVal = UUID.randomUUID().toString() + Thread.currentThread().getName();
		try {
			Boolean isLock = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_GOODS_LOCK, lockVal, 10L, TimeUnit.SECONDS);
			if (!isLock) {
				return "分布式锁抢占失败，请重试...";
			}
			result = decrGoods(pid);
		} finally {
			String currentLock = stringRedisTemplate.opsForValue().get(REDIS_GOODS_LOCK);
			if (lockVal.equals(currentLock)) {
				stringRedisTemplate.delete(REDIS_GOODS_LOCK);
			}
		}

		return result;
	}

	/**
	 * 1. 使用事务解锁
	 * 2. 使用 Lua 脚本解锁
	 *
	 * 问题是：无法确保业务在过期时间内完成，必须创建后台线程锁续期
	 * 		使用 redisson
	 *
	 * @param pid goodsId
	 * @return str
	 */
	@GetMapping("/buy3/{pid}")
	public String buyGoods3(@PathVariable String pid) {
		String result, lockVal = UUID.randomUUID().toString() + Thread.currentThread().getName();
		try {
			Boolean isLock = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_GOODS_LOCK, lockVal, 10L, TimeUnit.SECONDS);
			if (!isLock) {
				return "分布式锁抢占失败，请重试...";
			}
			result = decrGoods(pid);
		} finally {
			// transactionDeleteLock(lockVal);
			luaDeleteLock(lockVal);
		}

		return result;
	}

	/**
	 * redisson 分布式锁
	 *
	 * @param pid goodsId
	 * @return str
	 */
	@GetMapping("/buy4/{pid}")
	public String buyGoods4(@PathVariable String pid) {
		RLock lock = redisson.getLock(REDIS_GOODS_LOCK);
		lock.lock();
		try {
			return decrGoods(pid);
		} finally {
			/*
			 * 直接 unlock() 可能会出现 bug
			 */
			if (lock.isLocked() && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	// =======================================================================================
	// divide
	// =======================================================================================

	/**
	 * 购买商品
	 *
	 * @param pid goodsId
	 * @return rs
	 */
	private String decrGoods(String pid) {
		String result, rinventory = stringRedisTemplate.opsForValue().get((GOODS_PREFIX + pid));
		int inventory = null == rinventory ? 0 : Integer.parseInt(rinventory);
		if (inventory > 0) {
			int remain = inventory - 1;
			stringRedisTemplate.opsForValue().set(GOODS_PREFIX + pid, String.valueOf(remain));
			result = String.format(" ....购买成功， 还剩：%d ，服务提供者为：%s", remain, port);
			log.info(result);
		} else {
			result = "商品下架或售罄..." + "服务提供者为 " + port;
		}
		return result;
	}

	/**
	 * 使用 redis 事务，判断锁并删除锁
	 * 自旋，如果被其它线程修改过了，则再次获取删除
	 *
	 * @param currentLockVal currentLockVal
	 */
	private void transactionDeleteLock(String currentLockVal) {
		for (; ; ) {
			stringRedisTemplate.setEnableTransactionSupport(true);
			stringRedisTemplate.watch(REDIS_GOODS_LOCK);
			// begin
			stringRedisTemplate.multi();
			if (currentLockVal.equalsIgnoreCase(stringRedisTemplate.opsForValue().get(REDIS_GOODS_LOCK))) {
				stringRedisTemplate.delete(REDIS_GOODS_LOCK);
			}
			// commit
			List<Object> exec = stringRedisTemplate.exec();
			if (CollectionUtils.isEmpty(exec)) {
				continue;
			}
			stringRedisTemplate.unwatch();
			break;
		}
	}

	/**
	 * lua 脚本原子性获取并删除锁
	 *
	 * @param currentLockVal currentLockval
	 */
	private void luaDeleteLock(String currentLockVal) {
		RedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT);
		Long rs = stringRedisTemplate.execute(redisScript,
				Collections.singletonList(REDIS_GOODS_LOCK), Collections.singletonList(currentLockVal));
		if ("1".equals(String.valueOf(rs))) {
			log.info(" del redis lock success ...");
		} else {
			log.info(" del redis lock failed ...");
		}
	}
}
