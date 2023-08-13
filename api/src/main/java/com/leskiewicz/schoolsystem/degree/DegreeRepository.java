package com.leskiewicz.schoolsystem.degree;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long> {

  List<Degree> findByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy);

  @Query(
      "SELECT d FROM Degree d WHERE d.faculty.name = :facultyName AND d.title = :title AND d.fieldOfStudy = :fieldOfStudy")
  Optional<Degree> findByFacultyNameAndTitleAndFieldOfStudy(
      String facultyName, DegreeTitle title, String fieldOfStudy);

  @Query("SELECT d FROM Degree d WHERE d.faculty.id = :facultyId")
  Page<Degree> findDegreesByFacultyId(Long facultyId, Pageable pageable);

  @Query(
      "SELECT d FROM Degree d WHERE (:facultyId is null or d.faculty.id = :facultyId) "
          + "AND (:title is null or d.title = :title) "
          + "AND (:fieldOfStudy is null or d.fieldOfStudy LIKE CONCAT('%', :fieldOfStudy, '%'))")
  Page<Degree> searchByFacultyNameAndTitleAndFieldOfStudy(
      @Param("fieldOfStudy") String fieldOfStudy,
      @Param("facultyId") Long facultyId,
      @Param("title") DegreeTitle title,
      Pageable pageable);
}
