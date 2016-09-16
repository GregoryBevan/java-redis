package com.elgregos.java.redis.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.elgregos.java.redis.aspect.LogTime;
import com.elgregos.java.redis.entities.SimpleEntity;
import com.elgregos.java.redis.service.SimpleEntityService;

@Component
public class SimpleEntityCache {

	@Configuration
	static class SimpleEntityCacheConfig {

		@Autowired
		private RedisConnectionFactory jedisConnectionFactory;

		@Bean
		CacheManager cacheManagerForList() {
			final RedisCacheManager cacheManager = new RedisCacheManager(redisTemplateForList());
			cacheManager.setDefaultExpiration(86400);
			return cacheManager;
		}

		@Bean
		RedisTemplate<String, List<SimpleEntity>> redisTemplateForList() {
			final RedisTemplate<String, List<SimpleEntity>> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory);
			redisTemplate.setDefaultSerializer(new JdkSerializationRedisSerializer());
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			return redisTemplate;
		}
	}

	public static final String SIMPLE_ENTITIES = "simple-entities";

	@Autowired
	private SimpleEntityService simpleEntityService;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private CacheManager cacheManagerForList;

	@LogTime
	public void loadCache() {
		for (final SimpleEntity simpleEntity : simpleEntityService.getSimpleEntities()) {
			cacheManager.getCache(SIMPLE_ENTITIES).putIfAbsent(simpleEntity.getCode(), simpleEntity);
		}
	}

	@LogTime
	public void loadListCache() {
		cacheManagerForList.getCache("list").put("maliste", simpleEntityService.getSimpleEntities());
	}

}
