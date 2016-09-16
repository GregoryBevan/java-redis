package com.elgregos.java.redis.entities.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elgregos.java.redis.test.TestConfig;

/**
 * Hierarchy value test. The test runs with a local database. Fixed values in
 * the test could vary because the populate as some random parameters
 *
 * @author gbe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@ActiveProfiles({ "test" })
public class HierarchyValueRepositoryTest {

	@Autowired
	private HierarchyValueRepository hierarchyValueRepository;

	@Test
	public void findByHierarchyIdPenultimateLevelAndFilter_Capital() {
		final Long hierarchyId = 3601L;
		final String filter = "";
		final List<HierarchyValue> hierarchyValues = hierarchyValueRepository
				.findByHierarchyIdPenultimateLevelAndFilter(hierarchyId, filter);
		assertEquals(11, hierarchyValues.size());
		assertEquals(new Long(3610), hierarchyValues.get(0).getId());
	}

	@Test
	public void testFindByHierarchyCode() {
		final List<HierarchyValue> hierarchyValues = hierarchyValueRepository.findByHierarchyCode("H0");
		assertNotNull(hierarchyValues);
		assertEquals(57, hierarchyValues.size());
	}

}
