package com.yan.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CommonTool {
	/**
	 * 去除表中重复的元素
	 * 
	 * @param list
	 * @return
	 */
	public static List<Object> removeDuplicateWithOrder(List<?> list) {
		Set<Object> set = new HashSet<Object>();
		List<Object> newList = new ArrayList<Object>();
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}
		return newList;
	}
}
