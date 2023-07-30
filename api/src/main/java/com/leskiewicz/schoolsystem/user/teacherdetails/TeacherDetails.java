package com.leskiewicz.schoolsystem.user.teacherdetails;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.user.User;
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
