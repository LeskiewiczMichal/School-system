package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.utils.Language;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "degree")
public class Degree {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "title")
  @Enumerated(EnumType.STRING)
  private DegreeTitle title;

  @NotNull
  @Column(name = "field_of_study")
  private String fieldOfStudy;

  @NotNull
  @Column(name = "length_of_study")
  private Double lengthOfStudy;

  @NotNull
  @Column(name = "tuition_fee_per_year")
  private Double tuitionFeePerYear;

  @ElementCollection(targetClass = Language.class)
  @CollectionTable(name = "languages_table", joinColumns = @JoinColumn(name = "degree_id"))
  @NotNull
  @Enumerated(EnumType.STRING)
  private List<Language> language;

  @NotNull
  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "description")
  private String description;

  @NotNull
  @Singular
  @ManyToMany
  @JoinTable(
      name = "degree_course",
      joinColumns = @JoinColumn(name = "degree_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"))
  private List<Course> courses;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "faculty", referencedColumnName = "id")
  private Faculty faculty;

  @Override
  public String toString() {
    return "Degree{"
        + "id="
        + id
        + ", title="
        + title
        + ", fieldOfStudy='"
        + fieldOfStudy
        + '\''
        + ", lengthOfStudy="
        + lengthOfStudy
        + ", tuitionFeePerYear="
        + tuitionFeePerYear
        + ", languages="
        + language
        + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Degree degree = (Degree) o;
    return Objects.equals(id, degree.id)
        && title == degree.title
        && Objects.equals(fieldOfStudy, degree.fieldOfStudy)
        && Objects.equals(faculty, degree.faculty)
        && Objects.equals(description, degree.description)
        && Objects.equals(lengthOfStudy, degree.lengthOfStudy)
        && Objects.equals(tuitionFeePerYear, degree.tuitionFeePerYear);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, fieldOfStudy, courses, faculty);
  }
}
