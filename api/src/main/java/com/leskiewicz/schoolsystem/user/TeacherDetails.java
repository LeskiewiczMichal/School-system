package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teacher_details")
public class TeacherDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @OneToOne
  @Column(name = "teacher_id")
  private User teacher;

  @NotNull
  @Column(name = "degree_field")
  private String degreeField;

  @NotNull
  @Column(name = "title")
  private DegreeTitle title;

  @Column(name = "bio")
  private String bio;

  @Column(name = "tutorship")
  private String tutorship;
}
