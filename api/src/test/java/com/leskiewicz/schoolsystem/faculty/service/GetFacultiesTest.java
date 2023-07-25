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

  //  @Test
  //  public void getFacultiesReturnsPagedFaculties() {
  //    Degree degree = Mockito.mock(Degree.class);
  //    List<Faculty> facultyList =
  //        Arrays.asList(
  //            Faculty.builder()
  //                .id(1L)
  //                .name("Software Engineering")
  //                .degrees(Collections.singletonList(degree))
  //                .build(),
  //            Faculty.builder()
  //                .id(2L)
  //                .name("Computer Science")
  //                .degrees(Collections.singletonList(degree))
  //                .build());
  //    Page<Faculty> usersPage = new PageImpl<>(facultyList);
  //
  //    given(facultyRepository.findAll(any(Pageable.class))).willReturn(usersPage);
  //
  //    // Mock the behavior of the userMapper
  //    FacultyDto facultyDto1 = new FacultyDto(1L, "Software Engineering");
  //    FacultyDto facultyDto2 = new FacultyDto(2L, "Computer Science");
  //    given(facultyMapper.convertToDto(any(Faculty.class))).willReturn(facultyDto1, facultyDto2);
  //
  //    // Call the method to test
  //    Page<FacultyDto> result = facultyService.getFaculties(PageRequest.of(0, 10));
  //
  //    // Assert the result
  //    Assertions.assertEquals(2, result.getTotalElements());
  //    Assertions.assertEquals(facultyDto1, result.getContent().get(0));
  //    Assertions.assertEquals(facultyDto2, result.getContent().get(1));
  //
  //    // Verify the interactions with userRepository and userMapper
  //    verify(facultyRepository, times(1)).findAll(any(Pageable.class));
  //    verify(facultyMapper, times(2)).convertToDto(any(Faculty.class));
  //  }
}
