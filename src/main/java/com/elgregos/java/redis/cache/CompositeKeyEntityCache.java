package com.elgregos.java.redis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.elgregos.java.redis.aspect.LogTime;
import com.elgregos.java.redis.entities.CompositeKeyEntity;
import com.elgregos.java.redis.entities.key.DoubleKey;
import com.elgregos.java.redis.service.CompositeKeyEntityService;

@Component
public class CompositeKeyEntityCache {

	public static final String COMPOSITE_KEY_ENTITIES = "composite-key-entities";

	@Autowired
	private CompositeKeyEntityService compositeKeyEntityService;

	@Autowired
	private CacheManager cacheManager;

	@LogTime
	public void loadCache() {
		for (final CompositeKeyEntity compositeKeyEntity : compositeKeyEntityService.getCompositeKeyEntities()) {
			cacheManager.getCache(COMPOSITE_KEY_ENTITIES).putIfAbsent(
					new DoubleKey(compositeKeyEntity.getFirstCode(), compositeKeyEntity.getSecondCode()),
					compositeKeyEntity);
		}
	}

}
