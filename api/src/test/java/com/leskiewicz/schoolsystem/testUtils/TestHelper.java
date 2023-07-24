package com.leskiewicz.schoolsystem.testUtils;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testModels.UserDto;
import com.leskiewicz.schoolsystem.user.User;

public class TestHelper {

  public static User createUser(Faculty faculty, Degree degree) {
    return User.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .email("johndoe@example.com")
        .password("12345")
        .role(Role.ROLE_STUDENT)
        .faculty(faculty)
        .degree(degree)
        .build();
  }

  public static User createTeacher(Faculty faculty) {
    return User.builder()
        .id(1L)
        .firstName("Daro")
        .lastName("Bibini")
        .email("darobibini@example.com")
        .password("12345")
        .role(Role.ROLE_TEACHER)
        .faculty(faculty)
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

  public static Course createCourse(Faculty faculty, User teacher) {
    return Course.builder()
        .id(1L)
        .title("TestCourse")
        .faculty(faculty)
        .teacher(teacher)
        .duration_in_hours(20)
        .build();
  }

  public static CourseDto createCourseDto(String facultyName, String teacherName) {
    return CourseDto.builder()
            .id(1L)
            .title("TestCourseDto")
            .faculty(facultyName)
            .teacher(teacherName)
            .facultyId(1L)
            .teacherId(1L)
            .durationInHours(20)
            .build();
  }
}
