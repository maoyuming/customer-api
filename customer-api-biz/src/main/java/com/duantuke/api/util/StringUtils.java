package com.duantuke.api.util;

import java.util.List;

public class StringUtils {
	public static String listToString(List list, char separator) {
		return org.apache.commons.lang.StringUtils.join(list.toArray(),separator);    
	}
}
