package com.leskiewicz.schoolsystem.builders;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.course.utils.CourseMapperImpl;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapperImpl;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.utils.Language;
import jakarta.persistence.*;
import java.util.List;

public class DegreeBuilder {

  private Long id = 1L;
  private DegreeTitle title = DegreeTitle.BACHELOR_OF_SCIENCE;
  private String fieldOfStudy = "Computer Science";
  private Double lengthOfStudy = 3.0;
  private Double tuitionFeePerYear = 10000.0;
  private List<Language> language = List.of(Language.ENGLISH);
  private String description = "Description";
  private String imageName = "default.png";
  private List<Course> courses = List.of();
  private Faculty faculty = new FacultyBuilder().build();

  public static DegreeBuilder aDegree() {
    return new DegreeBuilder();
  }

  public static DegreeDto degreeDtoFrom(Degree degree) {
    DegreeMapper degreeMapper = new DegreeMapperImpl();
    return degreeMapper.convertToDto(degree);
  }

  public DegreeBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public DegreeBuilder title(DegreeTitle title) {
    this.title = title;
    return this;
  }

  public DegreeBuilder fieldOfStudy(String fieldOfStudy) {
    this.fieldOfStudy = fieldOfStudy;
    return this;
  }

  public DegreeBuilder lengthOfStudy(Double lengthOfStudy) {
    this.lengthOfStudy = lengthOfStudy;
    return this;
  }

  public DegreeBuilder tuitionFeePerYear(Double tuitionFeePerYear) {
    this.tuitionFeePerYear = tuitionFeePerYear;
    return this;
  }

  public DegreeBuilder language(List<Language> language) {
    this.language = language;
    return this;
  }

  public DegreeBuilder description(String description) {
    this.description = description;
    return this;
  }

  public DegreeBuilder imageName(String imageName) {
    this.imageName = imageName;
    return this;
  }

  public DegreeBuilder courses(List<Course> courses) {
    this.courses = courses;
    return this;
  }

  public DegreeBuilder faculty(Faculty faculty) {
    this.faculty = faculty;
    return this;
  }

  public Degree build() {
    return Degree.builder()
        .id(id)
        .title(title)
        .fieldOfStudy(fieldOfStudy)
        .lengthOfStudy(lengthOfStudy)
        .tuitionFeePerYear(tuitionFeePerYear)
        .language(language)
        .description(description)
        .imageName(imageName)
        .courses(courses)
        .faculty(faculty)
        .build();
  }
}
