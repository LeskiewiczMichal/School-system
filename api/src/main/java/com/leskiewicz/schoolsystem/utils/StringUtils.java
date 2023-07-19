package com.leskiewicz.schoolsystem.utils;

public class StringUtils {

  public static String maskEmail(String email) {
    int atIndex = email.indexOf("@");
    if (atIndex == -1) {
      return email;
    }
    String username = email.substring(0, atIndex);
    String domain = email.substring(atIndex);
    int usernameLength = username.length();
    if (usernameLength <= 2) {
      return email;
    }
    int numCharsToMask = (int) Math.ceil(usernameLength / 2.0);
    int maskStartIndex = (usernameLength - numCharsToMask) / 2;
    char[] maskedUsernameChars = username.toCharArray();
    for (int i = maskStartIndex; i < maskStartIndex + numCharsToMask; i++) {
      maskedUsernameChars[i] = '*';
    }
    String maskedUsername = new String(maskedUsernameChars);
    return maskedUsername + domain;
  }

  public static String maskInfo(String info) {
    if (info == null || info.length() <= 2) {
      return info;
    }
    int length = info.length();
    return info.substring(0, 1)
        + "*".repeat(length - 2)
        + info.substring(length - 1);
  }
}
