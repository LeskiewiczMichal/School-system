package com.leskiewicz.schoolsystem.builders;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import jakarta.persistence.*;

public class TeacherDetailsBuilder {
  private Long id = 1L;
  private User teacher = new UserBuilder().build();
  private String degreeField = "Computer Science";
  private DegreeTitle title = DegreeTitle.PROFESSOR;
  private String bio = "Example of teacher bio";
  private String tutorship = "Example of tutorship";

  public static TeacherDetailsBuilder aTeacherDetails() {
    return new TeacherDetailsBuilder();
  }

  public TeacherDetailsBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public TeacherDetailsBuilder teacher(User teacher) {
    this.teacher = teacher;
    return this;
  }

  public TeacherDetailsBuilder degreeField(String degreeField) {
    this.degreeField = degreeField;
    return this;
  }

  public TeacherDetailsBuilder title(DegreeTitle title) {
    this.title = title;
    return this;
  }

  public TeacherDetailsBuilder bio(String bio) {
    this.bio = bio;
    return this;
  }

  public TeacherDetailsBuilder tutorship(String tutorship) {
    this.tutorship = tutorship;
    return this;
  }

  public TeacherDetails build() {
    return TeacherDetails.builder()
        .id(id)
        .teacher(teacher)
        .degreeField(degreeField)
        .title(title)
        .bio(bio)
        .tutorship(tutorship)
        .build();
  }
}
