package com.elgregos.java.redis.entities.common;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class CodeLabelEntity implements Serializable {

	private static final long serialVersionUID = -5415808983052049836L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Version
	private Long version;

	protected String code;

	protected String label;

	public String getCode() {
		return code;
	}

	public Long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public Long getVersion() {
		return version;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
