package com.leskiewicz.schoolsystem.course;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "title")
  private String title;

  @Column(name = "duration_in_hours")
  private int duration_in_hours;

  @Column(name = "description")
  private String description;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "faculty", referencedColumnName = "id")
  private Faculty faculty;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "teacher", referencedColumnName = "id")
  private User teacher;

  @NotNull
  @Singular
  @ManyToMany
  @JoinTable(
      name = "course_student",
      joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"))
  private List<User> students;

  @Singular
  @ManyToMany
  @JoinTable(
      name = "course_file",
      joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "file_id", referencedColumnName = "id"))
  private List<File> files;

  @Override
  public String toString() {
    return "Course{"
        + "id="
        + id
        + ", title='"
        + title
        + ", duration= "
        + duration_in_hours
        + "h"
        + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Course course = (Course) o;
    return Objects.equals(id, course.id)
        && Objects.equals(title, course.title)
        && Objects.equals(duration_in_hours, course.duration_in_hours)
        && Objects.equals(faculty, course.faculty);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, duration_in_hours, faculty);
  }
}
