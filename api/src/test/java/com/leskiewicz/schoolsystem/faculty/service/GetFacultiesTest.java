package com.leskiewicz.schoolsystem.faculty.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class GetFacultiesTest {

  @Mock private FacultyRepository facultyRepository;
  @Mock private FacultyMapper facultyMapper;
  @InjectMocks private FacultyServiceImpl facultyService;

  @Test
  public void getFacultiesReturnsPagedFaculties() {
    List<Faculty> faculties = Arrays.asList(TestHelper.createFaculty(), TestHelper.createFaculty());
    List<FacultyDto> facultyDtos =
        Arrays.asList(
            TestHelper.createFacultyDto("TestFaculty"), TestHelper.createFacultyDto("TestFaculty"));

    CommonTests.serviceGetAll(
        Faculty.class,
        faculties,
        facultyDtos,
        facultyRepository::findAll,
        facultyMapper::convertToDto,
        facultyService::getFaculties);
  }

}
