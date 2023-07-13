package com.leskiewicz.schoolsystem.repository;

import com.leskiewicz.schoolsystem.model.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long> {

    Optional<Degree> findByTitleAndFieldOfStudy(String title, String fieldOfStudy);
}
