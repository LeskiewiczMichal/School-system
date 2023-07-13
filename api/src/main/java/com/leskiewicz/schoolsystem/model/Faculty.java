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
@Table(name = "faculty")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "faculty")
    private List<Course> courses;

    @OneToMany(mappedBy = "faculty")
    private List<Degree> degrees;


    @ManyToMany
    @JoinTable(
            name = "faculty_teacher",
            joinColumns = @JoinColumn(name = "faculty_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    )
    private List<User> teachers;

    @OneToMany
    @JoinColumn(name = "student_faculty", referencedColumnName = "id")
    private List<User> students;
}
