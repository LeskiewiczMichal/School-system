package com.leskiewicz.schoolsystem.degree;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long> {

  List<Degree> findByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy);

  @Query(
      "SELECT d FROM Degree d WHERE d.faculty.name = :facultyName AND d.title = :title AND d.fieldOfStudy = :fieldOfStudy")
  Optional<Degree> findByFacultyNameAndTitleAndFieldOfStudy(
      String facultyName, DegreeTitle title, String fieldOfStudy);
}
