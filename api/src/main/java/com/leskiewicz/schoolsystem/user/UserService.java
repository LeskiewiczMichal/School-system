package com.leskiewicz.schoolsystem.user;

import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  UserDto getById(Long id);

  Page<UserDto> getUsers(Pageable pageable);

  void addUser(User user);

  User getByEmail(String email);

  UserDto updateUser(PatchUserRequest request, Long userId);

  Faculty getUserFaculty(Long userId);

  Degree getUserDegree(Long userId);

  Page<CourseDto> getUserCourses(Long userId, Pageable pageable);
}
