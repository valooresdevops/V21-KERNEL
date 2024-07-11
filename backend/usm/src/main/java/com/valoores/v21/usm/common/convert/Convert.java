package com.valoores.v21.usm.common.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Convert {
	
	public static String[] toStringArray(Set<String> setOfString) {

		// Create String[] of size of setOfString
		String[] arrayOfString = new String[setOfString.size()];

		// Copy elements from set to string array
		// using advanced for loop
		int index = 0;
		for (String str : setOfString)
			arrayOfString[index++] = str;

		// return the formed String[]
		return arrayOfString;
	}

	public static List<Long> toLongList(String[] list) {

		//convert String[] to long list
		List<Long> longList = new ArrayList<>();
		for (int i = 0; i < list.length; i++) {
			longList.add(Long.valueOf(list[i]));
		}
		return longList;
	}
}
