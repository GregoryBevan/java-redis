package com.elgregos.java.redis.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CompositeKeyEntity implements BaseEntity {

	private static final long serialVersionUID = 4621950803464175880L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String firstCode;
	private String secondCode;
	private String label;

	public String getFirstCode() {
		return firstCode;
	}

	public Long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getSecondCode() {
		return secondCode;
	}

	public void setFirstCode(String firstCode) {
		this.firstCode = firstCode;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setSecondCode(String secondCode) {
		this.secondCode = secondCode;
	}
}
