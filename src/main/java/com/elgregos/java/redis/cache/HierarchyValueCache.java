package com.elgregos.java.redis.cache;

import java.util.ArrayList;
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
import org.springframework.stereotype.Component;

import com.elgregos.java.redis.aspect.LogTime;
import com.elgregos.java.redis.cache.serializer.HierarchyValueKeySerialier;
import com.elgregos.java.redis.cache.serializer.ValueSerializer;
import com.elgregos.java.redis.entities.hierarchy.HierarchyValue;
import com.elgregos.java.redis.service.HierarchyService;
import com.elgregos.java.redis.service.HierarchyValueService;

@Component
public class HierarchyValueCache {

	@Configuration
	static class OtherHierarchyValueCacheConfig {

		@Autowired
		private RedisConnectionFactory jedisConnectionFactory;

		@Autowired
		private HierarchyValueKeySerialier hierarchyValueKeySerialier;

		@Autowired
		private ValueSerializer valueSerializer;

		@Bean
		ValueOperations<Long, HierarchyValue> hierarchyValueCacheOperations() {
			return otherHierarchyValueRedisTemplate().opsForValue();
		}

		@Bean
		RedisTemplate<Long, HierarchyValue> otherHierarchyValueRedisTemplate() {
			final RedisTemplate<Long, HierarchyValue> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory);
			redisTemplate.setKeySerializer(hierarchyValueKeySerialier);
			redisTemplate.setValueSerializer(valueSerializer);
			return redisTemplate;
		}
	}

	@Autowired
	private HierarchyService hierarchyService;

	@Autowired
	private HierarchyValueService hierarchyValueService;

	@Autowired
	private ValueOperations<Long, HierarchyValue> hierarchyValueCacheOperations;

	@LogTime
	public HierarchyValue get(Long id) {
		return hierarchyValueCacheOperations.get(id);
	}

	@LogTime
	public List<HierarchyValue> getWithMultiGet(List<Long> randomIds) {
		final List<HierarchyValue> hierarchyValues = new ArrayList<>(randomIds.size());
		for (final Long id : randomIds) {
			hierarchyValues.add(hierarchyValueCacheOperations.get(id));
		}
		return hierarchyValues;
	}

	@LogTime
	public List<HierarchyValue> getWithOneGet(List<Long> randomIds) {
		return hierarchyValueCacheOperations.multiGet(randomIds);
	}

	@LogTime
	public void loadCache() {
		hierarchyService.getAllHierarchyCodes().forEach(hierarchyCode -> loadCache(hierarchyCode));
	}

	public void loadCache(String hierarchyCode) {
		final List<HierarchyValue> hierarchyValues = hierarchyValueService.findByHierarchyCode(hierarchyCode);
		final Map<Long, HierarchyValue> map = hierarchyValues.stream()
				.collect(Collectors.toMap(HierarchyValue::getId, Function.identity()));

		hierarchyValueCacheOperations.multiSet(map);
	}

}
