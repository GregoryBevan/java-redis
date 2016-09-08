package com.elgregos.java.redis.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.elgregos.java.redis.entities.CompositeKeyEntity;
import com.elgregos.java.redis.entities.SimpleEntity;
import com.elgregos.java.redis.entities.key.DoubleKey;
import com.elgregos.java.redis.service.CompositeKeyEntityService;
import com.elgregos.java.redis.service.SimpleEntityService;

@Service
public class CacheLoader {

	private final Logger logger = LoggerFactory.getLogger(CacheLoader.class);

	@Autowired
	private SimpleEntityService simpleEntityService;

	@Autowired
	private CompositeKeyEntityService compositeKeyEntityService;

	@Autowired
	private CacheManager cacheManager;

	public void loadCache() {
		logger.info("Load Redis distributed cache");

		for (final SimpleEntity simpleEntity : simpleEntityService.getSimpleEntities()) {
			cacheManager.getCache("simple-entities").putIfAbsent(simpleEntity.getCode(), simpleEntity);
		}

		for (final CompositeKeyEntity compositeKeyEntity : compositeKeyEntityService.getCompositeKeyEntities()) {
			cacheManager.getCache("composite-key-entities").putIfAbsent(
					new DoubleKey(compositeKeyEntity.getFirstCode(), compositeKeyEntity.getSecondCode()),
					compositeKeyEntity);
		}
	}

}
