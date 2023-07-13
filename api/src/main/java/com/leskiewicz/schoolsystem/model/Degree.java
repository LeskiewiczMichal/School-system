package com.leskiewicz.schoolsystem.model;

import com.leskiewicz.schoolsystem.model.enums.DegreeTitle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
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

    @Column(name = "title")
    @Enumerated(EnumType.STRING)
    private DegreeTitle title;

    @Column(name = "field_of_study")
    private String fieldOfStudy;

    @ManyToMany
    @JoinTable(
            name = "degree_course",
            joinColumns = @JoinColumn(name = "degree_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )
    private List<Course> courses;

    @ManyToOne
    @JoinColumn(name = "faculty", referencedColumnName = "id")
    private Faculty faculty;
}
