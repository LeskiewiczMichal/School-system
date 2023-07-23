package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacultyService {

  FacultyDto getById(Long id);

  Faculty getByName(String name);

  Page<FacultyDto> getFaculties(Pageable pageable);

  Degree getDegreeByTitleAndFieldOfStudy(Faculty faculty, DegreeTitle title, String fieldOfStudy);

  FacultyDto createFaculty(CreateFacultyRequest request);

  FacultyDto updateFaculty(PatchFacultyRequest request, Long facultyId);

  Page<UserDto> getFacultyUsers(Long facultyId, Pageable pageable, Role role);
  Page<DegreeDto> getFacultyDegrees(Long facultyId, Pageable pageable);
}
