package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import jakarta.persistence.*;
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

    @Column(name="teacher_id")
    private Long teacherId;

    @Column(name = "degree_field")
    private String degreeField;

    @Column(name = "title")
    private DegreeTitle title;

}
