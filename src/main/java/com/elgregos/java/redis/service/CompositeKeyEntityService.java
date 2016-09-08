package com.elgregos.java.redis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.elgregos.java.redis.entities.CompositeKeyEntity;
import com.elgregos.java.redis.entities.key.DoubleKey;

@Service
public class CompositeKeyEntityService {

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	public List<CompositeKeyEntity> getCompositeKeyEntities() {
		// Create List of simple entity
		final List<CompositeKeyEntity> entities = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			final CompositeKeyEntity entity = new CompositeKeyEntity();
			entity.setFirstCode("CC");
			entity.setSecondCode("0" + i);
			entity.setLabel("Simple entity 0" + i);
			entities.add(entity);
		}
		return entities;
	}

	public CompositeKeyEntity loadFromCache(DoubleKey doubleKey) {
		return (CompositeKeyEntity) cacheManager.getCache("composite-key-entities").get(doubleKey).get();
	}

}
