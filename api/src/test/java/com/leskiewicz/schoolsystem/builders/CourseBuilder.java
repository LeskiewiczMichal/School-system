package com.leskiewicz.schoolsystem.builders;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseScope;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.course.utils.CourseMapperImpl;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.utils.Language;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Singular;

import java.util.List;

public class CourseBuilder {

  private Long id = 1L;
  private String title = "Computer Science";
  private int duration_in_hours = 10;
  private String description = "Example of course description";

  private Language language = Language.ENGLISH;
  private List<CourseScope> scope = List.of(CourseScope.LECTURES);
  private int ECTS = 5;
  private Faculty faculty = new FacultyBuilder().build();
  private User teacher =
      new UserBuilder()
          .role(Role.ROLE_TEACHER)
          .teacherDetails(new TeacherDetailsBuilder().build())
          .build();
  private List<User> students = List.of();
  private List<File> files = List.of();

  public static CourseBuilder aCourse() {
    return new CourseBuilder();
  }

  public static CourseDto courseDtoFrom(Course course) {
    CourseMapper courseMapper = new CourseMapperImpl();
    return courseMapper.convertToDto(course);
  }

  public CourseBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public CourseBuilder title(String title) {
    this.title = title;
    return this;
  }

  public CourseBuilder duration_in_hours(int duration_in_hours) {
    this.duration_in_hours = duration_in_hours;
    return this;
  }

  public CourseBuilder description(String description) {
    this.description = description;
    return this;
  }

  public CourseBuilder language(Language language) {
    this.language = language;
    return this;
  }

  public CourseBuilder scope(List<CourseScope> scope) {
    this.scope = scope;
    return this;
  }

  public CourseBuilder ECTS(int ECTS) {
    this.ECTS = ECTS;
    return this;
  }

  public CourseBuilder faculty(Faculty faculty) {
    this.faculty = faculty;
    return this;
  }

  public CourseBuilder teacher(User teacher) {
    this.teacher = teacher;
    return this;
  }

  public CourseBuilder students(List<User> students) {
    this.students = students;
    return this;
  }

  public CourseBuilder files(List<File> files) {
    this.files = files;
    return this;
  }

  public Course build() {
    return Course.builder()
        .id(id)
        .title(title)
        .duration_in_hours(duration_in_hours)
        .description(description)
        .language(language)
        .scope(scope)
        .ECTS(ECTS)
        .faculty(faculty)
        .teacher(teacher)
        .students(students)
        .files(files)
        .build();
  }
}
