package com.elgregos.java.redis.service;

import java.util.ArrayList;
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
	public List<SimpleEntity> getSimpleEntities() {
		return repository.findAll();
	}

	// @LogTime
	public SimpleEntity loadFromCache(Long id) {
		return simpleEntityCache.get(id);
	}

	public void testMulti(Long number) {
		final List<Long> randomIds = getIds(number);
		final List<SimpleEntity> withOneGet = simpleEntityCache.getWithOneGet(randomIds);
		final List<SimpleEntity> withMultiGet = simpleEntityCache.getWithMultiGet(randomIds);
		System.out.println("Sizes : " + withOneGet.size() + " & " + withMultiGet.size());
	}

	private List<Long> getIds(Long number) {
		final List<Long> ids = new ArrayList<>();
		for (int i = 1; i <= number; i++) {
			ids.add(Long.valueOf(i));
		}
		return ids;
	}
}
