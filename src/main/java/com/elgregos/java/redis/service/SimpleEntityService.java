package com.elgregos.java.redis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elgregos.java.redis.aspect.LogTime;
import com.elgregos.java.redis.cache.SimpleEntityCache;
import com.elgregos.java.redis.entities.SimpleEntity;
import com.elgregos.java.redis.entities.repositories.SimpleEntityRepository;

@Service
public class SimpleEntityService {

	@Autowired
	private SimpleEntityRepository repository;

	@Autowired
	private SimpleEntityCache simpleEntityCache;

	@LogTime
	public List<SimpleEntity> getAllFromCache() {
		return simpleEntityCache.getAllFromCache();
	}

	@LogTime
	public List<SimpleEntity> getSimpleEntities() {
		return repository.findAll();
	}

	@LogTime
	public SimpleEntity loadFromCache(String code) {
		return simpleEntityCache.get(code);
	}
}
