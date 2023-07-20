package com.leskiewicz.schoolsystem.utils;

public class StringUtils {

  public static String capitalizeFirstLetterOfEveryWord(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }
    String[] words = input.split("\\s+");
    StringBuilder result = new StringBuilder();
    for (String word : words) {
      result.append(word.substring(0, 1).toUpperCase())
          .append(word.substring(1).toLowerCase())
          .append(" ");
    }
    return result.toString().trim();
  }
}

