package com.elgregos.java.redis.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.elgregos.java.redis.cache.CacheFlusher;
import com.elgregos.java.redis.cache.CacheLoader;

@RestController
@RequestMapping("cache-handler")
public class CacheHandlerRestImpl {

	@Autowired
	private CacheLoader cacheLoader;

	@Autowired
	private CacheFlusher cacheFlusher;

	@RequestMapping(method = RequestMethod.POST, value = "flush")
	public String flush() {
		cacheFlusher.flushCache();
		return "Cache flushed";
	}

	@RequestMapping(method = RequestMethod.POST, value = "load")
	public String load() {
		cacheLoader.loadCache();
		return "Cache loaded";
	}

}
