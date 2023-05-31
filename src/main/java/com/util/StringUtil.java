package com.util;

public class StringUtil {

	public static boolean isNull(String value) {
		boolean retVal = true;
		
		if (value != null) {
			if (!"".equals(value.trim())) {
				retVal = false;
			}
		}
		
		return retVal;
	}
	
	public static String getUpDown(String value) {
		String retVal = Const.UPDOWN_NONE;	
		
		if (!StringUtil.isNull(value)) {
			if (value.contains("+")) {
				retVal = Const.UPDOWN_UP;	
			} else if (value.contains("-")) {
				retVal = Const.UPDOWN_DOWN;
			}
		}

		return retVal;
	}
	
	public static String deleteSpecialSymbol(String value) {
		String[] specialSymbol = {",", "%", "\\+", "-", "\""};
		if (!StringUtil.isNull(value)) {
			for (int i = 0; i < specialSymbol.length; i++) {
				value = value.replaceAll(specialSymbol[i], "");
			}
		}
		return value;
	}
}
