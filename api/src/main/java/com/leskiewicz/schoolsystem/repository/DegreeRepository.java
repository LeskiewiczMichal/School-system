package com.leskiewicz.schoolsystem.repository;

import com.leskiewicz.schoolsystem.model.Degree;
import com.leskiewicz.schoolsystem.model.enums.DegreeTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long> {

    Optional<Degree> findByTitleAndFieldOfStudy(DegreeTitle title, String fieldOfStudy);
}
