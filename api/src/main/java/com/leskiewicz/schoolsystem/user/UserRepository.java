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
}
