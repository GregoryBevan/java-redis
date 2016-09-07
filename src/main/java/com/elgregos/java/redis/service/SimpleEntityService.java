package com.elgregos.java.redis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.elgregos.java.redis.entities.SimpleEntity;

@Service
public class SimpleEntityService {

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
}
