package com.pluralsight.bookstore.util;

public class TextUtil {

    public String saniitize(String textToSanitize){
        return textToSanitize.replaceAll("\\s+", " ");
    }
}
