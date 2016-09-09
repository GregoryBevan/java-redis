package com.elgregos.java.redis.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SimpleEntity implements BaseEntity {

	private static final long serialVersionUID = 4244751097030627290L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String code;
	private String label;

	@Column(columnDefinition = "TEXT")
	private String description;

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
