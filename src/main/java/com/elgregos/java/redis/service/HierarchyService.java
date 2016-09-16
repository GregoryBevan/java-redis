package com.elgregos.java.redis.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elgregos.java.redis.entities.hierarchy.Hierarchy;
import com.elgregos.java.redis.entities.hierarchy.HierarchyRepository;

@Service
public class HierarchyService {

	@Autowired
	private HierarchyRepository repository;

	public Collection<Hierarchy> getAllHierarchies() {
		return repository.findAll();
	}

	public Collection<String> getAllHierarchyCodes() {
		final Collection<Hierarchy> hierarchies = getAllHierarchies();
		return hierarchies.stream().map(Hierarchy::getCode).collect(Collectors.toList());
	}

}
