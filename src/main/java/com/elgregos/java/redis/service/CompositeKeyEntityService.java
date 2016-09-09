package com.elgregos.java.redis.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.elgregos.java.redis.entities.CompositeKeyEntity;
import com.elgregos.java.redis.entities.key.DoubleKey;
import com.elgregos.java.redis.entities.repositories.CompositeKeyEntityRepository;

@Service
public class CompositeKeyEntityService {

	private final Logger logger = LoggerFactory.getLogger(CompositeKeyEntityService.class);

	@Autowired
	private CompositeKeyEntityRepository repository;

	@Autowired
	private CacheManager cacheManager;

	// @Autowired
	// private RedisTemplate<Object, Object> redisTemplate;

	public List<CompositeKeyEntity> getCompositeKeyEntities() {
		logger.info("");
		return this.repository.findAll();
	}

	public CompositeKeyEntity loadFromCache(DoubleKey doubleKey) {
		return (CompositeKeyEntity) cacheManager.getCache("composite-key-entities").get(doubleKey).get();
	}

}
