package com.example.utils;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

public class DBUtilsTest {

    @Test
    public void mapToString() {
        final String expected = "one two, three four, five six";

        final Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("one", "two");
        map.put("three", "four");
        map.put("five", "six");
        final String actual = DBUtils.mapToString(map);
        assertThat(actual).isEqualTo(expected);
    }
}