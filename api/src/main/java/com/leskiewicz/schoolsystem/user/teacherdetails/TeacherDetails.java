package com.leskiewicz.schoolsystem.user.teacherdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

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
}
