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
import com.elgregos.java.redis.cache.serializer.SimpleEntityKeySerialier;
import com.elgregos.java.redis.cache.serializer.ValueSerializer;
import com.elgregos.java.redis.entities.SimpleEntity;
import com.elgregos.java.redis.service.SimpleEntityService;

@Component
public class SimpleEntityCache {

	@Configuration
	static class SimpleEntityCacheConfig {

		@Autowired
		private RedisConnectionFactory jedisConnectionFactory;

		@Autowired
		private ValueSerializer valueSerializer;

		@Autowired
		private SimpleEntityKeySerialier keySerializer;

		@Bean
		public ValueOperations<Long, SimpleEntity> simpleEntityValueOperations() {
			return simpleEntityRedisTemplate().opsForValue();
		}

		@Bean
		RedisTemplate<Long, SimpleEntity> simpleEntityRedisTemplate() {
			final RedisTemplate<Long, SimpleEntity> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory);
			redisTemplate.setKeySerializer(keySerializer);
			redisTemplate.setValueSerializer(valueSerializer);
			return redisTemplate;
		}
	}

	@Autowired
	private SimpleEntityService simpleEntityService;

	@Autowired
	private ValueOperations<Long, SimpleEntity> simpleEntityValueOperations;

	@LogTime
	public SimpleEntity get(Long id) {
		return simpleEntityValueOperations.get(id);
	}

	@LogTime
	public List<SimpleEntity> getWithMultiGet(List<Long> randomIds) {
		final List<SimpleEntity> simpleEntities = new ArrayList<>(randomIds.size());
		for (final Long id : randomIds) {
			simpleEntities.add(simpleEntityValueOperations.get(id));
		}
		return simpleEntities;
	}

	@LogTime
	public List<SimpleEntity> getWithOneGet(List<Long> randomIds) {
		return simpleEntityValueOperations.multiGet(randomIds);
	}

	@LogTime
	public void loadCache() {
		final Map<Long, SimpleEntity> simpleEntitiesMap = simpleEntityService.getSimpleEntities().stream()
				.collect(Collectors.toMap(SimpleEntity::getId, Function.identity()));
		simpleEntityValueOperations.multiSet(simpleEntitiesMap);
	}
}
