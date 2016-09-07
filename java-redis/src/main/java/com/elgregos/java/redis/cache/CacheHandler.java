package com.elgregos.java.redis.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class CacheHandler {

	private static final JedisPool JEDIS_POOL = new JedisPool();

	private CacheHandler() {

	}

	public static Jedis getJedisInstance() {
		return JEDIS_POOL.getResource();
	}

}
