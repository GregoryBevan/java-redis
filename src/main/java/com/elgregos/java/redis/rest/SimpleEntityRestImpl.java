package com.elgregos.java.redis.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.elgregos.java.redis.entities.SimpleEntity;
import com.elgregos.java.redis.service.SimpleEntityService;

@RestController
@RequestMapping("simple-entities")
public class SimpleEntityRestImpl {

	@Autowired
	private SimpleEntityService service;

	@RequestMapping(method = RequestMethod.GET, value = "/{code}")
	public SimpleEntity get(@PathVariable("code") String code) {
		return service.loadFromCache(code);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<SimpleEntity> getAll() {
		return service.getSimpleEntities();
	}

	@RequestMapping(method = RequestMethod.GET, path = "/all")
	public List<SimpleEntity> getAllFromCache() {
		return service.getAllFromCache();
	}

}
