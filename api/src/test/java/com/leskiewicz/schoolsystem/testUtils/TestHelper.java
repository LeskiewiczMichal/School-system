package com.leskiewicz.schoolsystem.testUtils;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;

import java.util.Arrays;
import java.util.List;

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

  public static TeacherDetails createTeacherDetails(User teacher) {
    return TeacherDetails.builder()
        .bio("I am a teacher")
        .degreeField("Computer Science")
        .title(DegreeTitle.DOCTOR)
        .tutorship("I am a tutor")
        .id(1L)
        .teacher(teacher)
        .build();
  }

  public static UserDto createUserDto(User user) {
    return UserDto.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .faculty(user.getFaculty().getName())
        .degree(user.getDegree().getFieldOfStudy())
        .build();
  }

  public static UserDto createUserDto(String facultyName, String degreeFieldOfStudy) {
    return UserDto.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .email("johndoe@example.com")
        .faculty(facultyName)
        .degree(degreeFieldOfStudy)
        .build();
  }

  //  public static UserDto createUserDto(Faculty faculty, Degree degree) {
  //    return UserDto.builder()
  //            .id(1L)
  //            .firstName("John")
  //            .lastName("Doe")
  //            .email("johndoe@example.com")
  //            .faculty(faculty.getName())
  //            .degree(degree.getFieldOfStudy())
  //            .build();
  //  }

  public static Faculty createFaculty() {
    return Faculty.builder().id(1L).name("TestFaculty").build();
  }

  public static FacultyDto createFacultyDto(String name) {
    return FacultyDto.builder().id(1L).name(name).build();
  }

  public static Degree createDegree(Faculty faculty) {
    return Degree.builder()
        .id(1L)
        .title(DegreeTitle.BACHELOR_OF_SCIENCE)
        .fieldOfStudy("Computer Science")
        .faculty(faculty)
        .build();
  }

  public static DegreeDto createDegreeDto(String facultyName) {
    return DegreeDto.builder()
        .id(1L)
        .title(DegreeTitle.BACHELOR_OF_SCIENCE)
        .fieldOfStudy("Computer Science")
        .faculty(facultyName)
        .facultyId(1L)
        .build();
  }

  /// *** Course *** ///
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

  public static CourseDto createCourseDto(Course course) {
    return CourseDto.builder()
        .id(course.getId())
        .title(course.getTitle())
        .faculty(course.getFaculty().getName())
        .teacher(course.getTeacher().getFirstName() + " " + course.getTeacher().getLastName())
        .facultyId(course.getFaculty().getId())
        .teacherId(course.getTeacher().getId())
        .durationInHours(course.getDuration_in_hours())
        .build();
  }

  public static List<Course> createCoursesList(Faculty faculty, User teacher) {
    return Arrays.asList(
        Course.builder()
            .id(1L)
            .title("Software Engineering")
            .faculty(faculty)
            .duration_in_hours(20)
            .teacher(teacher)
            .build(),
        Course.builder()
            .id(2L)
            .title("Computer Science")
            .faculty(faculty)
            .duration_in_hours(30)
            .teacher(teacher)
            .build());
  }

  public static List<CourseDto> createCoursesDtosList(Course course1, Course course2) {
    return Arrays.asList(
        CourseDto.builder()
            .id(course1.getId())
            .title(course1.getTitle())
            .faculty(course1.getFaculty().getName())
            .teacher(course1.getTeacher().getFirstName() + " " + course1.getTeacher().getLastName())
            .durationInHours(course1.getDuration_in_hours())
            .build(),
        CourseDto.builder()
            .id(course2.getId())
            .title(course2.getTitle())
            .faculty(course2.getFaculty().getName())
            .teacher(course2.getTeacher().getFirstName() + " " + course2.getTeacher().getLastName())
            .durationInHours(course2.getDuration_in_hours())
            .build());
  }
}
