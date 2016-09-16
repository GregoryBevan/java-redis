package com.elgregos.java.redis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elgregos.java.redis.aspect.LogTime;

@Service
public class CacheLoader {

	@Autowired
	private SimpleEntityCache simpleEntityCache;

	@Autowired
	private CompositeKeyEntityCache compositeKeyEntityCache;

	@Autowired
	private HierarchyValueCache hierarchyValueCache;

	@LogTime
	public void loadCache() {
		// simpleEntityCache.loadCache();
		// simpleEntityCache.loadListCache();
		// compositeKeyEntityCache.loadCache();
		hierarchyValueCache.loadCache();
	}

}
