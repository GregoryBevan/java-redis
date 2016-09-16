package com.elgregos.java.redis;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import redis.clients.jedis.Jedis;

public class JedisClient {

	private final Jedis jedis;

	public JedisClient() {
		jedis = new Jedis();
		jedis.connect();
	}

	public static void main(String... args) {
		final JedisClient jedisClient = new JedisClient();
		jedisClient.getValue("hvoh_CGV_ARTI");
	}

	public Object getValue(String key) {
		new JdkSerializationRedisSerializer().deserialize(jedis.get(key.getBytes()));
		return jedis.get(key);
	}

}
