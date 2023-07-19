package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "faculty")
public class Faculty extends RepresentationModel<Faculty> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "faculty")
    private List<Course> courses;

    @OneToMany(mappedBy = "faculty")
    private List<Degree> degrees;

    @OneToMany(mappedBy = "faculty")
    private List<User> users;


    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(id, faculty.id) &&
                Objects.equals(name, faculty.name) &&
                Objects.equals(courses, faculty.courses) &&
                Objects.equals(degrees, faculty.degrees) &&
                Objects.equals(users, faculty.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, courses, degrees, users);
    }
}
