package com.elgregos.java.redis.cache;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.elgregos.java.redis.aspect.LogTime;
import com.elgregos.java.redis.conf.CustomRedisSerializer;
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
public class OtherHierarchyValueCache {

	@Configuration
	static class OtherHierarchyValueCacheConfig {

		@Autowired
		private RedisConnectionFactory jedisConnectionFactory;

		@Autowired
		private CustomRedisSerializer customRedisSerializer;

		@Bean
		ValueOperations<String, HierarchyValue> hierarchyValueCacheOperations() {
			return otherHierarchyValueRedisTemplate().opsForValue();
		}

		// RedisTemplate<String, Map<Long, HierarchyValue>>

		@Bean
		RedisTemplate<String, HierarchyValue> otherHierarchyValueRedisTemplate() {
			final RedisTemplate<String, HierarchyValue> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory);
			final ObjectMapper mapper = new ObjectMapper();
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			mapper.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
			mapper.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE);
			mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
			mapper.registerModule(new JodaModule());
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.setValueSerializer(customRedisSerializer);
			return redisTemplate;
		}
	}

	private static final String KEY_PREFIX = "ohv_";

	@Autowired
	private HierarchyService hierarchyService;

	@Autowired
	private HierarchyValueService hierarchyValueService;

	@Autowired
	private ValueOperations<String, HierarchyValue> hierarchyValueCacheOperations;

	@LogTime
	public HierarchyValue getById(Long id) {
		return hierarchyValueCacheOperations.get(KEY_PREFIX.concat(id.toString()));
	}

	@LogTime
	public void loadCache() {
		hierarchyService.getAllHierarchyCodes().forEach(hierarchyCode -> loadCache(hierarchyCode));
	}

	private void loadCache(String hierarchyCode) {
		final List<HierarchyValue> hierarchyValues = hierarchyValueService.findByHierarchyCode(hierarchyCode);
		final Map<String, HierarchyValue> map = hierarchyValues.stream()
				.collect(Collectors.toMap(h -> KEY_PREFIX.concat(h.getId().toString()), Function.identity()));

		hierarchyValueCacheOperations.multiSet(map);
	}

}
