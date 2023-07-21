package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Optional<Faculty> findByName(String name);

    @Query("SELECT u FROM User u WHERE u.faculty.id = :facultyId AND u.role = :role")
    Page<User> findFacultyUsers(Long facultyId, Pageable pageable, Role role);
}

