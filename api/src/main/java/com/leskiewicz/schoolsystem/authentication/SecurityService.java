package com.leskiewicz.schoolsystem.authentication;

import org.springframework.security.core.Authentication;

public interface SecurityService {

//  boolean isSelf(Authentication authentication, Long id);

  boolean isCourseTeacher(Long courseId);
}
