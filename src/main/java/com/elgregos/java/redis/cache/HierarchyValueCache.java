package com.elgregos.java.redis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
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
		RedisTemplate<String, HierarchyValue> hierarchyValueRedisTemplate() {
			final RedisTemplate<String, HierarchyValue> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory);
			final ObjectMapper mapper = new ObjectMapper();
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			mapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
			mapper.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE);
			mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
			mapper.registerModule(new JodaModule());
			redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(mapper));
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			return redisTemplate;
		}
	}

	@Autowired
	private HierarchyService hierarchyService;

	@Autowired
	private HierarchyValueService hierarchyValueService;

	@Autowired
	private CacheManager hierarchyValueCacheManager;

	public HierarchyValue getById(Long id) {
		return (HierarchyValue) hierarchyValueCacheManager.getCache("hierarchy-values").get(getKey(id)).get();
	}

	@LogTime
	public void loadCache() {
		hierarchyService.getAllHierarchyCodes().forEach(hierarchyCode -> loadCache(hierarchyCode));
	}

	private String getKey(final Long id) {
		return "hv_" + id;
	}

	private void loadCache(String hierarchyCode) {
		hierarchyValueService.findByHierarchyCode(hierarchyCode).forEach(hierarchyValue -> {
			hierarchyValueCacheManager.getCache("hierarchy-values").put(getKey(hierarchyValue.getId()), hierarchyValue);
		});
	}

}
