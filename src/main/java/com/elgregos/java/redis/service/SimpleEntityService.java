package com.elgregos.java.redis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.elgregos.java.redis.entities.SimpleEntity;

@Service
public class SimpleEntityService {

	@Autowired
	private CacheManager cacheManager;

	public List<SimpleEntity> getSimpleEntities() {
		// Create List of simple entity
		final List<SimpleEntity> simpleEntities = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			final SimpleEntity simpleEntity = new SimpleEntity();
			simpleEntity.setCode("0" + i);
			simpleEntity.setLabel("Simple entity 0" + i);
			simpleEntities.add(simpleEntity);
		}
		return simpleEntities;
	}

	public SimpleEntity loadFromCache(String code) {
		return (SimpleEntity) cacheManager.getCache("simple-entities").get(code).get();
	}
}
