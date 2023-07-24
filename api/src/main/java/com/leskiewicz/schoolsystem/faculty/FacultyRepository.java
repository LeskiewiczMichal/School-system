package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.user.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

  Optional<Faculty> findByName(String name);

  @Query("SELECT u FROM User u WHERE u.faculty.id = :facultyId AND u.role = :role")
  Page<User> findFacultyUsers(Long facultyId, Pageable pageable, Role role);

  @Query("SELECT d FROM Degree d WHERE d.faculty.id = :facultyId")
  Page<Degree> findFacultyDegrees(Long facultyId, Pageable pageable);

}
