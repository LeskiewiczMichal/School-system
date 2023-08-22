package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  @Query("SELECT u.faculty FROM User u WHERE u.id = :id")
  Optional<Faculty> findFacultyByUserId(@Param("id") Long id);

  @Query("SELECT u.degree FROM User u WHERE u.id = :id")
  Optional<Degree> findDegreeByUserId(@Param("id") Long id);

  @Query("SELECT u FROM User u WHERE u.faculty.id = :facultyId AND u.role = :role")
  Page<User> findUsersByFacultyId(Long facultyId, Pageable pageable, Role role);

  @Query(
      value =
          "SELECT u.* FROM users u JOIN course_student cs ON u.id = cs.student_id WHERE cs.course_id = :courseId",
      nativeQuery = true)
  Page<User> findUsersByCourseId(Long courseId, Pageable pageable);

  @Query(
          "SELECT u FROM User u WHERE (:lastName is null or u.lastName LIKE CONCAT('%', :lastName, '%') OR u.firstName LIKE CONCAT('%', :lastName, '%')) " +
                  "AND (:firstName is null or u.firstName LIKE CONCAT('%', :firstName, '%') OR u.lastName LIKE CONCAT('%', :firstName, '%')) " +
                  "AND (:role is null or u.role = :role) ")
  Page<User> searchUsersByLastNameAndFirstNameAndRole(String lastName, String firstName, Role role, Pageable pageable);
}
