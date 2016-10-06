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

	@Autowired
	private OtherHierarchyValueCache otherHierarchyValueCache;

	@LogTime
	public void loadCache() {
		simpleEntityCache.loadCache();
		compositeKeyEntityCache.loadCache();
		hierarchyValueCache.loadCache();
		otherHierarchyValueCache.loadCache();
	}

}
