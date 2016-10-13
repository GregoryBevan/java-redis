package com.elgregos.java.redis.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class CacheConfiguration {

	@Bean
	public CustomRedisSerializer customRedisSerializer() {
		return new CustomRedisSerializer();
	}

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {

		final JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName("usupplychainredi.vif.fr");
		factory.setPort(6379);
		factory.setUsePool(true);
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(64);
		factory.setPoolConfig(poolConfig);
		return factory;
	}

}
