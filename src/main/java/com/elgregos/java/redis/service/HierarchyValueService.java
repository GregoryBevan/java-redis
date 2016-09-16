package com.elgregos.java.redis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elgregos.java.redis.entities.hierarchy.HierarchyValue;
import com.elgregos.java.redis.entities.hierarchy.HierarchyValueRepository;

@Service
public class HierarchyValueService {

	@Autowired
	private HierarchyValueRepository hierarchyValueRepository;

	public List<HierarchyValue> findByHierarchyCode(final String hierarchyCode) {
		return hierarchyValueRepository.findByHierarchyCode(hierarchyCode);
	}

}