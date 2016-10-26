package com.elgregos.java.redis.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elgregos.java.redis.aspect.LogTime;
import com.elgregos.java.redis.cache.HierarchyValueCache;
import com.elgregos.java.redis.entities.hierarchy.HierarchyValue;
import com.elgregos.java.redis.entities.hierarchy.HierarchyValueRepository;

@Service
public class HierarchyValueService {

	@Autowired
	private HierarchyValueCache cache;

	@Autowired
	private HierarchyValueRepository hierarchyValueRepository;

	@LogTime
	public List<HierarchyValue> findByHierarchyCode(final String hierarchyCode) {
		return hierarchyValueRepository.findByHierarchyCode(hierarchyCode);
	}

	public HierarchyValue getByIdFromCache(final Long id) {
		return cache.get(id);
	}

	public void testMulti(Long number) {
		final List<Long> randomIds = getIds(number);
		final List<HierarchyValue> withOneGet = cache.getWithOneGet(randomIds);
		final List<HierarchyValue> withMultiGet = cache.getWithMultiGet(randomIds);
		System.out.println("Sizes : " + withOneGet.size() + " & " + withMultiGet.size());
	}

	private List<Long> getIds(Long number) {
		final Pageable limit = new PageRequest(0, number.intValue());
		return StreamSupport.stream(hierarchyValueRepository.findAll(limit).spliterator(), false)
				.map(HierarchyValue::getId).collect(Collectors.toList());
	}
}
