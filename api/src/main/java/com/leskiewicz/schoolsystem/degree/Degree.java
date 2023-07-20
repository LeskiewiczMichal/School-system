package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Objects;

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
    @Singular
    @ManyToMany
    @JoinTable(
            name = "degree_course",
            joinColumns = @JoinColumn(name = "degree_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )
    private List<Course> courses;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "faculty", referencedColumnName = "id")
    private Faculty faculty;

    @Override
    public String toString() {
        return "Degree{" +
                "id=" + id +
                ", title=" + title +
                ", fieldOfStudy='" + fieldOfStudy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Degree degree = (Degree) o;
        return Objects.equals(id, degree.id) &&
                title == degree.title &&
                Objects.equals(fieldOfStudy, degree.fieldOfStudy) &&
                Objects.equals(courses, degree.courses) &&
                Objects.equals(faculty, degree.faculty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, fieldOfStudy, courses, faculty);
    }
}
