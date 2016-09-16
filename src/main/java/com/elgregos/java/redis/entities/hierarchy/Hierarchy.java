/*
 * Copyright (c) 2013 by VIF (Vignon Informatique France)
 * Project : supplychain-core-domain
 * File : $RCSfile$
 * Created on 10 oct. 2013 by ped
 */
package com.elgregos.java.redis.entities.hierarchy;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.elgregos.java.redis.entities.common.CodeLabelValidityEntity;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "code" }))
public class Hierarchy extends CodeLabelValidityEntity {

	private static final long serialVersionUID = -5240973036461711248L;

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
