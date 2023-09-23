package com.leskiewicz.schoolsystem.user.teacherdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teacher_details")
public class TeacherDetails extends RepresentationModel<TeacherDetails> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @OneToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinColumn(name = "teacher_id")
  private User teacher;

  @NotNull
  @Column(name = "degree_field")
  private String degreeField;

  @NotNull
  @Column(name = "title")
  @Enumerated(EnumType.STRING)
  private DegreeTitle title;

  @NotNull
  @Column(name = "bio")
  private String bio;

  @NotNull
  @Column(name = "tutorship")
  private String tutorship;

  public void update(PatchTeacherDetailsRequest request) {
    if (request.title() != null) {
      this.title = request.title();
    }
    if (request.bio() != null) {
      this.bio = request.bio();
    }
    if (request.degreeField() != null) {
      this.degreeField = request.degreeField();
    }
    if (request.tutorship() != null) {
      this.tutorship = request.tutorship();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TeacherDetails that)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(getId(), that.getId())
        && Objects.equals(getTeacher(), that.getTeacher())
        && Objects.equals(getDegreeField(), that.getDegreeField())
        && getTitle() == that.getTitle()
        && Objects.equals(getBio(), that.getBio())
        && Objects.equals(getTutorship(), that.getTutorship());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        super.hashCode(),
        getId(),
        getTeacher(),
        getDegreeField(),
        getTitle(),
        getBio(),
        getTutorship());
  }

  @Override
  public String toString() {
    return "TeacherDetails{"
        + "id="
        + id
        + ", teacher="
        + teacher
        + ", degreeField='"
        + degreeField
        + '\''
        + ", title="
        + title
        + ", bio='"
        + bio
        + '\''
        + ", tutorship='"
        + tutorship
        + '\''
        + '}';
  }
}
