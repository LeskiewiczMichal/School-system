package com.leskiewicz.schoolsystem.builders;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.user.User;
import java.util.ArrayList;
import java.util.List;

public class FacultyBuilder {

  private Long id = 1L;
  private String name = "Faculty of Computer Science";
  private List<Course> courses = new ArrayList<Course>();
  private List<Degree> degrees = new ArrayList<Degree>();
  private List<User> users = new ArrayList<User>();

  public static FacultyBuilder aFaculty() {
    return new FacultyBuilder();
  }

  public FacultyBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public FacultyBuilder name(String name) {
    this.name = name;
    return this;
  }

  public FacultyBuilder courses(List<Course> courses) {
    this.courses = courses;
    return this;
  }

  public FacultyBuilder degrees(List<Degree> degrees) {
    this.degrees = degrees;
    return this;
  }

  public FacultyBuilder users(List<User> users) {
    this.users = users;
    return this;
  }

  public Faculty build() {
    return Faculty.builder()
            .id(id)
            .name(name)
            .courses(courses)
            .degrees(degrees)
            .users(users)
            .build();
  }
}
