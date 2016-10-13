package com.elgregos.java.redis.populate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elgregos.java.redis.entities.CompositeKeyEntity;
import com.elgregos.java.redis.entities.repositories.CompositeKeyEntityRepository;

@Component
public class CompositeKeyEntityPopulate {

	@Autowired
	private CompositeKeyEntityRepository repository;

	public void populate() {
		final List<CompositeKeyEntity> entities = new ArrayList<>();
		for (char c = 'A'; c <= 'Z'; c++) {
			for (int i = 0; i < 10000; i++) {
				final CompositeKeyEntity entity = new CompositeKeyEntity();
				entity.setFirstCode(String.valueOf(c));
				entity.setSecondCode(String.valueOf(i));
				entity.setLabel("Composite key entity " + i);
				entity.setDescription(
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent blandit orci vitae nisl sollicitudin aliquet. Donec dignissim urna non cursus pretium. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Curabitur eu pulvinar magna. Vivamus dapibus purus nec mattis placerat. Curabitur tempus ante pellentesque interdum sollicitudin");
				entities.add(entity);
			}
		}
		repository.save(entities);

	}

}
