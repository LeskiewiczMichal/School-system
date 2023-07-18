package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
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

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "faculty")
    private List<Course> courses;

    @OneToMany(mappedBy = "faculty")
    private List<Degree> degrees;

    @OneToMany(mappedBy = "faculty")
    private List<User> users;
}
