package com.leskiewicz.schoolsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany
    @JoinColumn(name = "course_ids", referencedColumnName = "id")
    private List<Course> courses;

    @ManyToMany
    @JoinColumn(name = "teacher_ids", referencedColumnName = "id")
    private List<User> teachers;

    @OneToMany
    @JoinColumn(name = "student_ids", referencedColumnName = "id")
    private List<User> students;
}
