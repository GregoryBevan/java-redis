package com.elgregos.java.redis.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
public class CacheConfiguration {

	@Bean
	public CustomRedisSerializer customRedisSerializer() {
		return new CustomRedisSerializer();
	}

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {

		final JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName("localhost");
		factory.setPort(6379);
		factory.setUsePool(true);

		return factory;
	}

}
