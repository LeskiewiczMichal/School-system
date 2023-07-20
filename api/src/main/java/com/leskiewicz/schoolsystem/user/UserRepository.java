package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u.faculty FROM User u WHERE u.id = :id")
    Optional<Faculty> findFacultyByUserId(@Param("id") Long id);
}
