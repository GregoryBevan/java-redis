package com.elgregos.java.redis.populate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elgregos.java.redis.entities.SimpleEntity;
import com.elgregos.java.redis.entities.repositories.SimpleEntityRepository;

@Component
public class SimpleEntityPopulate {

	@Autowired
	private SimpleEntityRepository repository;

	public void populate() {
		final List<SimpleEntity> entities = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			final SimpleEntity entity = new SimpleEntity();
			entity.setCode(String.valueOf(i));
			entity.setLabel("Simple entity " + i);
			entity.setDescription(
					"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent blandit orci vitae nisl sollicitudin aliquet. Donec dignissim urna non cursus pretium. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Curabitur eu pulvinar magna. Vivamus dapibus purus nec mattis placerat. Curabitur tempus ante pellentesque interdum sollicitudin");
			entities.add(entity);
		}
		repository.save(entities);

	}

}
