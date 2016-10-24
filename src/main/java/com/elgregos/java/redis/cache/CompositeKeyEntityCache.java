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
import com.elgregos.java.redis.cache.serializer.CompositeKeyEntitySerialier;
import com.elgregos.java.redis.cache.serializer.ValueSerializer;
import com.elgregos.java.redis.entities.CompositeKeyEntity;
import com.elgregos.java.redis.entities.key.DoubleKey;
import com.elgregos.java.redis.service.CompositeKeyEntityService;

@Component
public class CompositeKeyEntityCache {

	@Configuration
	static class CompositeKeyEntityCacheConfig {

		@Autowired
		private RedisConnectionFactory jedisConnectionFactory;

		@Autowired
		private CompositeKeyEntitySerialier keySerializer;

		@Autowired
		private ValueSerializer customRedisSerializer;

		@Bean
		public ValueOperations<DoubleKey, CompositeKeyEntity> compositeKeyEntityValueOperations() {
			return compositeKeyEntityRedisTemplate().opsForValue();
		}

		@Bean
		RedisTemplate<DoubleKey, CompositeKeyEntity> compositeKeyEntityRedisTemplate() {
			final RedisTemplate<DoubleKey, CompositeKeyEntity> redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory);
			redisTemplate.setKeySerializer(keySerializer);
			redisTemplate.setValueSerializer(customRedisSerializer);
			return redisTemplate;
		}
	}

	public static final String COMPOSITE_KEY_ENTITIES = "composite-key-entities";

	@Autowired
	private CompositeKeyEntityService compositeKeyEntityService;

	@Autowired
	public ValueOperations<DoubleKey, CompositeKeyEntity> compositeKeyEntityValueOperations;

	@LogTime
	public CompositeKeyEntity get(DoubleKey key) {
		return compositeKeyEntityValueOperations.get(key);
	}

	@LogTime
	public List<CompositeKeyEntity> getWithMultiGet(List<DoubleKey> randomDoubleKeys) {
		final List<CompositeKeyEntity> compositeKeyEntities = new ArrayList<>(randomDoubleKeys.size());
		for (final DoubleKey doubleKey : randomDoubleKeys) {
			compositeKeyEntities.add(compositeKeyEntityValueOperations.get(doubleKey));
		}
		return compositeKeyEntities;
	}

	@LogTime
	public List<CompositeKeyEntity> getWithOneGet(List<DoubleKey> randomDoubleKeys) {
		return compositeKeyEntityValueOperations.multiGet(randomDoubleKeys);
	}

	@LogTime
	public void loadCache() {
		final Map<DoubleKey, CompositeKeyEntity> compositeEntityMap = compositeKeyEntityService
				.getCompositeKeyEntities().stream().collect(
						Collectors.toMap(c -> new DoubleKey(c.getFirstCode(), c.getSecondCode()), Function.identity()));
		compositeKeyEntityValueOperations.multiSet(compositeEntityMap);
	}

}
