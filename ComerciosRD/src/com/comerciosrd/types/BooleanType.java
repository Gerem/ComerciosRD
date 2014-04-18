package com.comerciosrd.types;

import android.annotation.SuppressLint;

@SuppressLint("UseValueOf")
public enum BooleanType {

	SI(new Integer(1),"SI"),
	NO(new Integer(0),"NO");
	
	private final Integer code;
	private final String value;
	
	BooleanType(Integer code, String value){
		this.code=code;
		this.value = value;
	}

	public Integer getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}
}
