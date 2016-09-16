/*
 * Copyright (c) 2015 by VIF (Vignon Informatique France)
 * Project : supplychain-core-domain
 * File : $RCSfile$
 * Created on 16 juin 2015 by ea
 */
package com.elgregos.java.redis.entities.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class Validity is used to handle logical deletion of entities.
 *
 * <u>This class creates immutable objects</u>
 *
 * {@link Validity#beginAt} are {@link Validity#endAt} are never null.
 */
@Embeddable
public class Validity implements Serializable {

	private static final long serialVersionUID = -763788156457147875L;

	public static final DateTime LAST_DAY_OF_2999 = new DateTime(2999, 12, 31, 0, 0, ISOChronology.getInstanceUTC());

	@Column(name = "vEndAt")
	private DateTime endAt;

	/**
	 * Instantiates a new validity, with dates to default value.
	 * {@link Validity#FIRST_DAY_OF_1970} as start date,
	 * {@link Validity#LAST_DAY_OF_2999} as end date.
	 */
	public Validity() {
		this(LAST_DAY_OF_2999);
	}

	/**
	 * Instantiates a new validity.
	 *
	 * @param beginAt
	 *            the begin at
	 * @param endAt
	 *            the end at
	 */
	public Validity(DateTime endAt) {
		super();
		if (endAt != null) {
			setEndAt(endAt);
		} else {
			setEndAt(LAST_DAY_OF_2999);
		}
	}

	/**
	 * This method doesn't modify the validity object because they are
	 * <u>immutable</u>, you MUST set the returned validity to your object.
	 *
	 *
	 * return a {@link Validity} with the begin date set at the same value as
	 * the original object and the enddate set to yesterday. Note : if your
	 * validity starts in the today or later the begin date will be overided by
	 * the yesterday date.
	 */
	public Validity disable() {
		final DateTime today = DateTime.now();
		final DateTime yesterday = today.minusDays(1);
		return new Validity(yesterday);
	}

	public DateTime getEndAt() {
		return endAt;
	}

	/**
	 * @return true, if {@link Validity#beginAt} <= today <=
	 *         {@link Validity#endAt}
	 */
	@JsonIgnore
	public boolean isValid() {
		final DateTime today = new DateTime();
		return today.isBefore(getEndAt()) || today.equals(getEndAt());
	}

	/**
	 * Re enable, checks if the validity is valid , if not it return a valid
	 * one.
	 *
	 * @return the validity with a start date at today and an end date
	 *         {@link Validity#LAST_DAY_OF_2999}
	 */
	public Validity reEnable() {
		if (isValid()) {
			throw new IllegalStateException("Impossible to enable an entity that is still valid.");
		}
		return new Validity();
	}

	/**
	 * Sets end date.
	 *
	 * @param endAt
	 *            the end at
	 */
	private void setEndAt(DateTime endAt) {
		this.endAt = endAt;
	}
}
