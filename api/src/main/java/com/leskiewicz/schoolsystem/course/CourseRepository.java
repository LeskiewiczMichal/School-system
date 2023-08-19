package com.leskiewicz.schoolsystem.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

  //    @Query("SELECT c FROM Course c WHERE c.id IN (SELECT uc.course_id FROM course_student uc
  // WHERE uc.user_id = :id)")
  //    Page<Course> findCoursesByUserId(@Param("id") Long id, Pageable pageable);

  @Query(
      "SELECT COUNT(c) > 0 FROM Course c WHERE c.title = :title AND c.duration_in_hours = :durationInHours AND c.teacher.id = :teacherId AND c.faculty.id = :facultyId")
  boolean existsCourseWithAttributes(
      @Param("title") String title,
      @Param("durationInHours") int durationInHours,
      @Param("teacherId") Long teacherId,
      @Param("facultyId") Long facultyId);

  @Query(
          value = "SELECT COUNT(c) > 0 FROM CourseStudent cs WHERE cs.course_id = :courseId AND cs.student_id = :userId", nativeQuery = true
  )
  boolean existsCourseStudentRelation(@Param("courseId") Long courseId, @Param("userId") Long userId);

  @Query(
      value =
          "SELECT c.* FROM course c JOIN course_student cs ON c.id = cs.course_id WHERE cs.student_id = :id",
      nativeQuery = true)
  Page<Course> findCoursesByUserId(@Param("id") Long userId, Pageable pageable);

  @Query(value = "SELECT c.* FROM course c WHERE c.teacher = :id", nativeQuery = true)
  Page<Course> findCoursesByTeacherId(@Param("id") Long userId, Pageable pageable);

  @Query("SELECT c FROM Course c WHERE c.faculty.id = :facultyId")
  Page<Course> findCoursesByFacultyId(Long facultyId, Pageable pageable);

  @Query(
      value =
          "SELECT c.* FROM course c JOIN degree_course dc ON c.id = dc.course_id WHERE dc.degree_id = :id",
      nativeQuery = true)
  Page<Course> findCoursesByDegreeId(@Param("id") Long degreeId, Pageable pageable);

  @Modifying
  @Query(value = "DELETE FROM degree_course dc WHERE dc.course_id = :courseId", nativeQuery = true)
  void deleteDegreeCourseRelationByCourseId(@Param("courseId") Long courseId);

  @Modifying
  @Query(value = "DELETE FROM course_student cs WHERE cs.course_id = :courseId", nativeQuery = true)
  void deleteCourseStudentRelationByCourseId(@Param("courseId") Long courseId);

  @Query(value = "SELECT c.description FROM Course c WHERE c.id = :courseId")
  String findCourseDescriptionById(Long courseId);
}
