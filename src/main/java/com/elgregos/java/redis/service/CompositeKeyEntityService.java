package com.elgregos.java.redis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.elgregos.java.redis.aspect.LogTime;
import com.elgregos.java.redis.entities.CompositeKeyEntity;
import com.elgregos.java.redis.entities.key.DoubleKey;
import com.elgregos.java.redis.entities.repositories.CompositeKeyEntityRepository;

@Service
public class CompositeKeyEntityService {

	@Autowired
	private CompositeKeyEntityRepository repository;

	@Autowired
	private CacheManager cacheManager;

	@LogTime
	public List<CompositeKeyEntity> getCompositeKeyEntities() {
		return this.repository.findAll();
	}

	public CompositeKeyEntity loadFromCache(DoubleKey doubleKey) {
		return (CompositeKeyEntity) cacheManager.getCache("composite-key-entities").get(doubleKey).get();
	}

}
