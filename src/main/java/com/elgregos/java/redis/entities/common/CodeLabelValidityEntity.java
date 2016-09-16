package com.elgregos.java.redis.entities.common;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class CodeLabelValidityEntity extends CodeLabelEntity {

	private static final long serialVersionUID = 3310025502282638766L;

	private Validity validity;

	public Validity getValidity() {
		return validity;
	}

	public void setValidity(Validity validity) {
		this.validity = validity;
	}

}