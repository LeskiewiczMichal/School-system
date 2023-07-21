package com.leskiewicz.schoolsystem.security;

import org.springframework.security.core.Authentication;

public interface SecurityService {

  boolean isSelf(Authentication authentication, Long id);
}
