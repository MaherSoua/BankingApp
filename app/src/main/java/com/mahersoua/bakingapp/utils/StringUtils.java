package com.mahersoua.bakingapp.utils;

public class StringUtils {
    public static final String DELIMETER = "->";

    public static String join(String delimeter, String[] list) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < list.length; i++){
            stringBuilder.append(list[i]);
            if(i < list.length) {
                stringBuilder.append(delimeter);
            }
        }
        return stringBuilder.toString();
    }
}

