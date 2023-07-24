package com.leskiewicz.schoolsystem.user;

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

  //  @Query("SELECT c FROM Course c WHERE c.id IN (SELECT uc.course_id FROM course_student uc WHERE
  // uc.user_id = :id)")
  //  Page<Course> findCoursesByUserId(@Param("id") Long id, Pageable pageable);

//  @Query(
//      value =
//          "SELECT u.* FROM users u JOIN course_student cs ON u.id = cs.student_id WHERE cs.course_id = :id",
//      nativeQuery = true)
//  Page<User> findStudentsByCourseId(@Param("id") Long id, Pageable pageable);
}
