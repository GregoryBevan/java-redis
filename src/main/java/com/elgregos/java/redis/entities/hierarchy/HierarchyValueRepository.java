/*
 * Copyright (c) 2013 by VIF (Vignon Informatique France)
 * Project : supplychain-core-domain
 * File : $RCSfile$
 * Created on 10 oct. 2013 by ped
 */
package com.elgregos.java.redis.entities.hierarchy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HierarchyValueRepository extends JpaRepository<HierarchyValue, Long> {

	@Query("SELECT hv FROM HierarchyValue hv WHERE hv.hierarchyLevel.hierarchy.code = :hierarchyCode")
	public List<HierarchyValue> findByHierarchyCode(@Param("hierarchyCode") String hierarchyCode);

	/**
	 * Return a list of all the {@link HierarchyValue} of the penultimate level
	 * of the hierarchy and which matches filter.
	 *
	 * <b>/!\ WARNING : the children are not fetched so each getChildren access
	 * is going to trigger a DB query!</b>
	 *
	 * @param hierarchyId
	 *            the ID of the hierarchy to select
	 * @param filter
	 *            characters to filter in the hierarchy values
	 *
	 * @return the hierarchy values list
	 */
	@Query("SELECT hv FROM HierarchyValue hv" + " WHERE hv.hierarchyLevel.hierarchy.id = :hierarchyId"
			+ " AND hv.hierarchyLevel.level = 1"
			+ " AND (upper(hv.code) LIKE %:filter% OR upper(hv.label) LIKE %:filter%) order by hv.code")
	List<HierarchyValue> findByHierarchyIdPenultimateLevelAndFilter(@Param("hierarchyId") Long hierarchyId,
			@Param("filter") String filter);
}