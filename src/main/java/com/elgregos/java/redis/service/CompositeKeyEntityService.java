package com.elgregos.java.redis.service;

import java.util.ArrayList;
import java.util.List;

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
		final List<DoubleKey> randomDoubleKeys = getDoubleKeys(number);
		final List<CompositeKeyEntity> withOneGet = cache.getWithOneGet(randomDoubleKeys);
		final List<CompositeKeyEntity> withMultiGet = cache.getWithMultiGet(randomDoubleKeys);
		System.out.println("Sizes : " + withOneGet.size() + " & " + withMultiGet.size());

	}

	private List<DoubleKey> getDoubleKeys(Long number) {
		final List<DoubleKey> doubleKeys = new ArrayList<>();
		char currentFirstCode = 'A';
		int currentSecondCode = 0;
		for (int i = 1; i <= number; i++) {
			doubleKeys.add(new DoubleKey(String.valueOf(currentFirstCode), String.valueOf(currentSecondCode)));
			if (currentSecondCode == 9999) {
				currentFirstCode = (char) (currentFirstCode + 1);
				currentSecondCode = 0;
				continue;
			}
			currentSecondCode += 1;
		}
		return doubleKeys;
	}

}
