package com.leskiewicz.schoolsystem.repository;

import com.leskiewicz.schoolsystem.model.User;
import com.leskiewicz.schoolsystem.model.excerpt.UserExcerpt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(excerptProjection = UserExcerpt.class)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
