package com.example.utils;

import java.util.Map;

public class DBUtils {
	public static String mapToString(final Map<String, String> columns){
		final StringBuilder builder = new StringBuilder();
		for (final String column : columns.keySet()) {
            builder.append(String.format("%s %s, ", column, columns.get(column)));
        }
		return builder.substring(0, builder.length() - 2);
	}
}