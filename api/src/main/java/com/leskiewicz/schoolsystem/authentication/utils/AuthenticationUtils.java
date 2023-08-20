package com.leskiewicz.schoolsystem.authentication.utils;

import com.leskiewicz.schoolsystem.authentication.dto.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtils {

  public CustomUserDetails getAuthenticatedUser() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      return (CustomUserDetails) (authentication.getPrincipal());
    } catch (Exception e) {
      return null;
    }
  }
}
