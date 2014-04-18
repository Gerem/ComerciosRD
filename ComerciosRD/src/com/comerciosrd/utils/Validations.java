package com.comerciosrd.utils;

import java.util.List;

public class Validations {

	public static boolean ValidateIsNotNull(Object o){
		if(o != null){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean ValidateIsNull(Object o){
		if(o == null){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean ValidateListIsNotNullAndNotEmpty(List list){
		if(list !=null && list.size()!=0){
			return true;
		}else{
			return false;
		}
	}
}
