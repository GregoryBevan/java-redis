/*
 * Copyright (c) 2013 by VIF (Vignon Informatique France)
 * Project : supplychain-core-domain
 * File : $RCSfile$
 * Created on 10 oct. 2013 by ped
 */
package com.elgregos.java.redis.entities.hierarchy;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HierarchyLevelRepository extends JpaRepository<HierarchyLevel, Long> {

}
