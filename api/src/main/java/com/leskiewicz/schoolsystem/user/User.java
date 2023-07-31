package com.leskiewicz.schoolsystem.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "first_name")
  private String firstName;

  @NotNull
  @Column(name = "last_name")
  private String lastName;

  @NotNull
  @Column(name = "email")
  private String email;

  @Column(name = "password")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "faculty", referencedColumnName = "id")
  private Faculty faculty;

  @ManyToOne
  @JoinColumn(name = "degree", referencedColumnName = "id")
  private Degree degree;

  @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private TeacherDetails teacherDetails;

  @NotNull
  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private Role role;

  @AssertTrue(message = "Degree must not be null for students")
  public boolean isDegreeValid() {
    return role != Role.ROLE_STUDENT || degree != null;
  }

  @Override
  public String toString() {
    return "User{"
        + "id="
        + id
        + ", firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", email='"
        + email
        + '\''
        + ", role="
        + role
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id)
        && Objects.equals(firstName, user.firstName)
        && Objects.equals(lastName, user.lastName)
        && Objects.equals(email, user.email)
        && Objects.equals(faculty, user.faculty)
        && Objects.equals(degree, user.degree)
        && role == user.role;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, email, faculty, degree, role);
  }
}
