package com.elgregos.java.redis.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.elgregos.java.redis.service.SimpleEntityService;

@RestController("simple-entities")
public class SimpleEntityRestImpl {

	@Autowired
	private SimpleEntityService service;

	// public Respons

}
