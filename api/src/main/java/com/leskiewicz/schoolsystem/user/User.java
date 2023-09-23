package com.leskiewicz.schoolsystem.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.error.ErrorMessages;
import com.leskiewicz.schoolsystem.error.customexception.UserAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.files.File;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
//@Setter
@Builder
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

  @Column(name = "profile_picture_name")
  private String profilePictureName;

  @AssertTrue(message = "Degree must not be null for students")
  public boolean isDegreeValid() {
    return role != Role.ROLE_STUDENT || degree != null;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

  public void update(PatchUserRequest request, PasswordEncoder passwordEncoder) {
    if (request.email() != null) {
      this.email = request.email();
    }

    if (request.firstName() != null) {
      this.firstName = request.firstName();
    }

    if (request.lastName() != null) {
      this.lastName = request.lastName();
    }

    if (request.password() != null) {
      this.password = passwordEncoder.encode(request.password());
    }
  }

  public void setProfilePictureName(String profilePictureName) {
    this.profilePictureName = profilePictureName;
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
