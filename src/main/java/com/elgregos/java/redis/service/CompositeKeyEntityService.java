package com.elgregos.java.redis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elgregos.java.redis.aspect.LogTime;
import com.elgregos.java.redis.cache.CompositeKeyEntityCache;
import com.elgregos.java.redis.entities.CompositeKeyEntity;
import com.elgregos.java.redis.entities.key.DoubleKey;
import com.elgregos.java.redis.entities.repositories.CompositeKeyEntityRepository;

@Service
public class CompositeKeyEntityService {

	@Autowired
	private CompositeKeyEntityRepository repository;

	@Autowired
	private CompositeKeyEntityCache cache;

	@LogTime
	public List<CompositeKeyEntity> getCompositeKeyEntities() {
		return this.repository.findAll();
	}

	public CompositeKeyEntity loadFromCache(DoubleKey key) {
		return cache.get(key);
	}

	public void testMulti(Long number) {
		final List<DoubleKey> randomDoubleKeys = getRandomDoubleKeys(number);
		final List<CompositeKeyEntity> withOneGet = cache.getWithOneGet(randomDoubleKeys);
		final List<CompositeKeyEntity> withMultiGet = cache.getWithMultiGet(randomDoubleKeys);
		System.out.println("Sizes : " + withOneGet.size() + " & " + withMultiGet.size());

	}

	private List<DoubleKey> getRandomDoubleKeys(Long number) {
		final Random randomObj = new Random();
		final List<DoubleKey> doubleKeys = new ArrayList<>();
		long count = 0;
		while (count < number) {
			final String firstCode = String.valueOf((char) randomObj.ints('A', 'Z').findFirst().getAsInt());
			final String secondCode = String.valueOf(randomObj.longs(1, 10000).findFirst().getAsLong());
			final DoubleKey doubleKey = new DoubleKey(firstCode, secondCode);
			doubleKeys.add(doubleKey);
			count++;
		}
		return doubleKeys;
	}

}
