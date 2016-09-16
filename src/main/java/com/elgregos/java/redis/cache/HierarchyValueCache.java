package com.elgregos.java.redis.cache;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.elgregos.java.redis.aspect.LogTime;
import com.elgregos.java.redis.entities.hierarchy.HierarchyValue;
import com.elgregos.java.redis.service.HierarchyService;
import com.elgregos.java.redis.service.HierarchyValueService;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@Component
public class HierarchyValueCache {

	@Configuration
	static class HierarchyValueCacheConfig {

		@Autowired
		private RedisConnectionFactory jedisConnectionFactory;

		@Bean
		CacheManager hierarchyValueCacheManager() {
			final RedisCacheManager cacheManager = new RedisCacheManager(hierarchyValueRedisTemplate());
			cacheManager.setDefaultExpiration(86400);
			return cacheManager;
		}

		@Bean
		RedisTemplate<String, String> hierarchyValueRedisTemplate() {
			final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory);
			final ObjectMapper mapper = new ObjectMapper();
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			mapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
			mapper.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE);
			mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
			mapper.registerModule(new JodaModule());
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.setHashKeySerializer(new GenericToStringSerializer<>(Long.class));
			redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(mapper));
			return redisTemplate;
		}
	}

	private static final String HIERARCHY_VALUES = "hierarchy-values";

	@Autowired
	private HierarchyService hierarchyService;

	@Autowired
	private HierarchyValueService hierarchyValueService;

	@Autowired
	private RedisTemplate<String, String> hierarchyValueRedisTemplate;

	private HashOperations<String, Long, HierarchyValue> opsForHash;

	public List<HierarchyValue> getAll() {
		return opsForHash.entries(HIERARCHY_VALUES).values().stream().collect(Collectors.toList());
	}

	public HierarchyValue getById(Long id) {
		return opsForHash.get(HIERARCHY_VALUES, id);
	}

	@PostConstruct
	public void init() {
		opsForHash = hierarchyValueRedisTemplate.opsForHash();
	}

	@LogTime
	public void loadCache() {
		hierarchyService.getAllHierarchyCodes().forEach(hierarchyCode -> loadCache(hierarchyCode));
	}

	private void loadCache(String hierarchyCode) {
		final List<HierarchyValue> hierarchyValues = hierarchyValueService.findByHierarchyCode(hierarchyCode);
		final Map<Long, HierarchyValue> map = hierarchyValues.stream()
				.collect(Collectors.toMap(HierarchyValue::getId, Function.identity()));

		opsForHash.putAll(HIERARCHY_VALUES, map);
	}

}
