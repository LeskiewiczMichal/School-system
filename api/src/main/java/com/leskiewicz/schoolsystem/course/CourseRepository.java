package com.leskiewicz.schoolsystem.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

  //    @Query("SELECT c FROM Course c WHERE c.id IN (SELECT uc.course_id FROM course_student uc
  // WHERE uc.user_id = :id)")
  //    Page<Course> findCoursesByUserId(@Param("id") Long id, Pageable pageable);

  @Query(
      value =
          "SELECT c.* FROM course c JOIN course_student cs ON c.id = cs.course_id WHERE cs.student_id = :id",
      nativeQuery = true)
  Page<Course> findCoursesByUserId(@Param("id") Long id, Pageable pageable);
}
