package com.leskiewicz.schoolsystem.testUtils;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.user.User;

public class TestHelper {

  public static User createUser(Faculty faculty, Degree degree) {
    return User.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .email("johndoe@example.com")
        .role(Role.ROLE_STUDENT)
        .faculty(faculty)
        .degree(degree)
        .build();
  }

  public static Faculty createFaculty() {
    return Faculty.builder()
        .id(1L)
        .name("TestFaculty")
        .build();
  }

}
