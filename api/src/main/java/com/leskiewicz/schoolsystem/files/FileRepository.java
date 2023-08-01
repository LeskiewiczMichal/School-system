package com.leskiewicz.schoolsystem.files;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

  @Query(
      value =
          "SELECT f.* FROM File f JOIN course_file cf ON f.id = cf.file_id WHERE cf.course_id = :courseId",
      nativeQuery = true)
  Page<File> findFilesByCourseId(Long courseId, Pageable pageable);
}
