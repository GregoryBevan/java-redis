package com.elgregos.java.redis.cache;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elgregos.java.redis.entities.hierarchy.HierarchyValue;
import com.elgregos.java.redis.test.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@ActiveProfiles({ "test" })
public class HierarchyValueCacheTest {

	@Autowired
	private HierarchyValueCache cache;

	@Test
	public void test_get() {
		final HierarchyValue hierarchyValue = cache.get(10254L);
		assertNotNull(hierarchyValue);
	}

}
