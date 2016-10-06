package com.elgregos.java.redis.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.elgregos.java.redis.entities.hierarchy.HierarchyValue;
import com.elgregos.java.redis.service.OtherHierarchyValueService;

@RestController
@RequestMapping("other-hierarchy-values")
public class OtherHierarchyValueRestImpl {

	@Autowired
	private OtherHierarchyValueService service;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public HierarchyValue get(@PathVariable("id") Long id) {
		return service.getByIdFromCache(id);
	}

}
