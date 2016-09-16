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
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.elgregos.java.redis.aspect.LogTime;
import com.elgregos.java.redis.entities.CompositeKeyEntity;
import com.elgregos.java.redis.entities.key.DoubleKey;
import com.elgregos.java.redis.service.CompositeKeyEntityService;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@Component
public class CompositeKeyEntityCache {

	@Configuration
	static class CompositeKeyEntityCacheConfig {

		@Autowired
		private RedisConnectionFactory jedisConnectionFactory;

		@Bean
		RedisTemplate<String, String> compositeKeyEntityRedisTemplate() {
			final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory);
			final ObjectMapper mapper = new ObjectMapper();
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			mapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
			mapper.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE);
			mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
			mapper.registerModule(new JodaModule());
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer(mapper));
			redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(mapper));
			return redisTemplate;
		}
	}

	public static final String COMPOSITE_KEY_ENTITIES = "composite-key-entities";

	@Autowired
	private CompositeKeyEntityService compositeKeyEntityService;

	@Autowired
	private RedisTemplate<String, String> compositeKeyEntityRedisTemplate;

	private HashOperations<String, DoubleKey, CompositeKeyEntity> opsForHash;

	@LogTime
	public CompositeKeyEntity get(DoubleKey key) {
		return opsForHash.get(COMPOSITE_KEY_ENTITIES, key);
	}

	@LogTime
	public List<CompositeKeyEntity> getAllFromCache() {
		return opsForHash.values(COMPOSITE_KEY_ENTITIES);
	}

	@PostConstruct
	public void init() {
		opsForHash = compositeKeyEntityRedisTemplate.opsForHash();
	}

	@LogTime
	public void loadCache() {
		final Map<DoubleKey, CompositeKeyEntity> compositeEntityMap = compositeKeyEntityService
				.getCompositeKeyEntities().stream().collect(
						Collectors.toMap(c -> new DoubleKey(c.getFirstCode(), c.getSecondCode()), Function.identity()));
		opsForHash.putAll(COMPOSITE_KEY_ENTITIES, compositeEntityMap);
	}

}
