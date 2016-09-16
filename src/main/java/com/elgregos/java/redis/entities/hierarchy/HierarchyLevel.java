/*
 * Copyright (c) 2013 by VIF (Vignon Informatique France)
 * Project : supplychain-core-domain
 * File : $RCSfile$
 * Created on 10 oct. 2013 by ped
 */
package com.elgregos.java.redis.entities.hierarchy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.elgregos.java.redis.entities.common.CodeLabelEntity;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hierarchy_id", "code" }))
public class HierarchyLevel extends CodeLabelEntity implements Serializable {

	private static final long serialVersionUID = 7732554622803287727L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "hierarchy_id")
	private Hierarchy hierarchy;

	@Column(name = "level_", nullable = false)
	private int level;

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public int getLevel() {
		return level;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}