package com.elgregos.java.redis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.elgregos.java.redis.aspect.LogTime;
import com.elgregos.java.redis.entities.SimpleEntity;
import com.elgregos.java.redis.entities.repositories.SimpleEntityRepository;

@Service
public class SimpleEntityService {

	@Autowired
	private SimpleEntityRepository repository;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private CacheManager cacheManagerForList;

	@LogTime
	public List<SimpleEntity> getSimpleEntities() {
		return repository.findAll();
	}

	public List<SimpleEntity> getSimpleEntitiesFromCache() {
		return (List<SimpleEntity>) cacheManagerForList.getCache("list").get("maliste");
	}

	public SimpleEntity loadFromCache(String code) {
		return (SimpleEntity) cacheManager.getCache("simple-entities").get(code).get();
	}
}
