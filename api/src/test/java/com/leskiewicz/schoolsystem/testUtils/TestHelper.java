package com.leskiewicz.schoolsystem.testUtils;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.testModels.UserDto;
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

  public static UserDto createUserDto(Faculty faculty, Degree degree) {
    return UserDto.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .email("johndoe@example.com")
        .faculty(faculty.getName())
        .degree(degree.getFieldOfStudy())
        .build();
  }

  public static Faculty createFaculty() {
    return Faculty.builder().id(1L).name("TestFaculty").build();
  }

  public static Degree createDegree(Faculty faculty) {
    return Degree.builder()
        .id(1L)
        .title(DegreeTitle.BACHELOR_OF_SCIENCE)
        .fieldOfStudy("Computer Science")
        .faculty(faculty)
        .build();
  }
}
