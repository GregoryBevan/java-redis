package com.elgregos.java.redis.entities.key;

public class DoubleKey {

	private String firstCode;

	private String secondCode;

	public DoubleKey(String firstCode, String secondCode) {
		this.firstCode = firstCode;
		this.secondCode = secondCode;
	}

	public String getFirstCode() {
		return firstCode;
	}

	public String getSecondCode() {
		return secondCode;
	}

	public void setFirstCode(String firstCode) {
		this.firstCode = firstCode;
	}

	public void setSecondCode(String secondCode) {
		this.secondCode = secondCode;
	}

}
