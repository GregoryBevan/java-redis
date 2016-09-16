/*
 * Copyright (c) 2013 by VIF (Vignon Informatique France)
 * Project : supplychain-core-domain
 * File : $RCSfile$
 * Created on 10 oct. 2013 by ped
 */
package com.elgregos.java.redis.entities.hierarchy;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.elgregos.java.redis.entities.common.CodeLabelEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hierarchylevel_id", "code" }))
public class HierarchyValue extends CodeLabelEntity {

	private static final long serialVersionUID = 2826207223308059227L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "hierarchylevel_id")
	private HierarchyLevel hierarchyLevel;

	@OneToMany(targetEntity = HierarchyValue.class, fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	@OrderBy("parent_id ASC")
	private final Set<HierarchyValue> children = new TreeSet<>();

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "parent_id")
	private HierarchyValue parent;

	@JsonIgnore
	public Set<HierarchyValue> getChildren() {
		return children;
	}

	@JsonIgnore
	public Hierarchy getHierarchy() {
		return hierarchyLevel.getHierarchy();
	}

	@JsonIgnore
	public String getHierarchyCode() {
		return hierarchyLevel.getHierarchy().getCode();
	}

	public HierarchyLevel getHierarchyLevel() {
		return hierarchyLevel;
	}

	@JsonIgnore
	public int getLevel() {
		return hierarchyLevel.getLevel();
	}

	// @JsonIgnore
	public HierarchyValue getParent() {
		return parent;
	}

	public void setHierarchyLevel(HierarchyLevel hierarchyLevel) {
		this.hierarchyLevel = hierarchyLevel;
	}

	public void setParent(HierarchyValue parent) {
		this.parent = parent;
	}
}