package com.leskiewicz.schoolsystem.degree;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long> {

  Optional<Degree> findByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy);
}
