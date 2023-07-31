package com.leskiewicz.schoolsystem.user.teacherdetails;

import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeacherDetailsRepository extends JpaRepository<TeacherDetails, Long> {

  @Query(
      value = "SELECT td.* FROM teacher_details td WHERE td.teacher_id = :userId",
      nativeQuery = true)
  Optional<TeacherDetails> findByUserId(Long userId);
}
