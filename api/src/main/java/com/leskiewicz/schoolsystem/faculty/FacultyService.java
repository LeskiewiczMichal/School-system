package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacultyService {

  Faculty getById(Long id);

  Faculty getByName(String name);

  Page<Faculty> getFaculties(Pageable pageable);

  Degree getDegreeByTitleAndFieldOfStudy(Faculty faculty, DegreeTitle title, String fieldOfStudy);

  Faculty createFaculty(CreateFacultyRequest request);

  Faculty updateFaculty(PatchFacultyRequest request, Long facultyId);

  Page<User> getFacultyUsers(Long facultyId, Pageable pageable, Role role);
  Page<Degree> getFacultyDegrees(Long facultyId, Pageable pageable);
}
