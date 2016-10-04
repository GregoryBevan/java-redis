package com.elgregos.java.redis.cache;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.elgregos.java.redis.aspect.LogTime;
import com.elgregos.java.redis.conf.CustomRedisSerializer;
import com.elgregos.java.redis.entities.SimpleEntity;
import com.elgregos.java.redis.service.SimpleEntityService;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@Component
public class SimpleEntityCache {

	@Configuration
	static class SimpleEntityCacheConfig {

		@Autowired
		private RedisConnectionFactory jedisConnectionFactory;

		@Autowired
		private CustomRedisSerializer customRedisSerializer;

		@Bean
		RedisTemplate<String, Map<String, SimpleEntity>> simpleEntityRedisTemplate() {
			final RedisTemplate<String, Map<String, SimpleEntity>> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory);
			final ObjectMapper mapper = new ObjectMapper();
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			mapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
			mapper.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE);
			mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
			mapper.registerModule(new JodaModule());
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.setHashKeySerializer(new StringRedisSerializer());
			redisTemplate.setHashValueSerializer(customRedisSerializer);
			return redisTemplate;
		}
	}

	public static final String SIMPLE_ENTITIES = "simple-entities";

	@Autowired
	private SimpleEntityService simpleEntityService;

	@Autowired
	private RedisTemplate<String, Map<String, SimpleEntity>> simpleEntityRedisTemplate;

	private HashOperations<String, String, SimpleEntity> opsForHash;

	@LogTime
	public SimpleEntity get(String code) {
		return opsForHash.get(SIMPLE_ENTITIES, code);
	}

	@LogTime
	public List<SimpleEntity> getAllFromCache() {
		return opsForHash.values(SIMPLE_ENTITIES);
	}

	@PostConstruct
	public void init() {
		opsForHash = simpleEntityRedisTemplate.opsForHash();
	}

	@LogTime
	public void loadCache() {
		final Map<String, SimpleEntity> simpleEntitiesMap = simpleEntityService.getSimpleEntities().stream()
				.collect(Collectors.toMap(SimpleEntity::getCode, Function.identity()));
		opsForHash.putAll(SIMPLE_ENTITIES, simpleEntitiesMap);
	}
}
