package com.elgregos.java.redis.populate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elgregos.java.redis.entities.common.Validity;
import com.elgregos.java.redis.entities.hierarchy.Hierarchy;
import com.elgregos.java.redis.entities.hierarchy.HierarchyLevel;
import com.elgregos.java.redis.entities.hierarchy.HierarchyLevelRepository;
import com.elgregos.java.redis.entities.hierarchy.HierarchyRepository;
import com.elgregos.java.redis.entities.hierarchy.HierarchyValue;
import com.elgregos.java.redis.entities.hierarchy.HierarchyValueRepository;

@Component
public class HierarchyValuePopulate {

	@Autowired
	private HierarchyRepository hierarchyRepository;

	@Autowired
	private HierarchyLevelRepository hierarchyLevelRepository;

	@Autowired
	private HierarchyValueRepository hierarchyValueRepository;

	public void populate() {

		final Map<Integer, List<HierarchyValue>> hierarchyValues = new HashMap<>();

		for (int i = 0, size = 113; i < size; i++) {
			final Hierarchy hierarchy = new Hierarchy();
			hierarchy.setCode("H" + i);
			hierarchy.setLabel("Hierarchy " + i);
			hierarchy.setValidity(new Validity(DateTime.parse("2017-06-18T06:05")));
			hierarchy.setDescription("dfsdgdgdfgdfgfgdf");
			hierarchyRepository.save(hierarchy);

			for (int j = 0, random1 = new Random().nextInt(7) + 1; j < random1; j++) {
				final HierarchyLevel hierarchyLevel = new HierarchyLevel();
				hierarchyLevel.setHierarchy(hierarchy);
				hierarchyLevel.setCode("L" + i + "-" + j);
				hierarchyLevel.setLabel("Level " + i + "-" + j);
				hierarchyLevel.setLevel(j);
				hierarchyLevelRepository.save(hierarchyLevel);
				hierarchyValues.put(j, new ArrayList<>());

				for (int k = 0, random2 = new Random().nextInt(800) + 1; k <= random2; k++) {
					final HierarchyValue hierarchyValue = new HierarchyValue();
					hierarchyValue.setCode("HV" + i + "-" + j + "-" + k);
					hierarchyValue.setLabel("Value " + i + "-" + j + "-" + k);
					hierarchyValue.setHierarchyLevel(hierarchyLevel);
					if (j > 0) {
						final List<HierarchyValue> list = hierarchyValues.get(j - 1);
						hierarchyValue.setParent(list.get(new Random().nextInt(list.size() > 10 ? 10 : list.size())));
					}
					hierarchyValues.get(j).add(hierarchyValue);
					hierarchyValueRepository.save(hierarchyValue);

				}
			}
		}
	}

}
