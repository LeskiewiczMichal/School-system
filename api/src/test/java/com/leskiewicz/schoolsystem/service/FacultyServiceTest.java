package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyModelAssembler;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {

  //region Mocks
  @Mock
  private FacultyRepository facultyRepository;

  @InjectMocks
  private FacultyServiceImpl facultyService;
  //endregion

  // Variables
  Faculty faculty;

  @BeforeEach
  public void setup() {

    // Set up test data
    faculty = Faculty.builder().id(1L).name("Software Engineering").build();
  }

  //region GetById tests
  @Test
  public void getByIdReturnsCorrectFaculty() {
    given(facultyRepository.findById(faculty.getId())).willReturn(Optional.of(faculty));

    Faculty testFaculty = facultyService.getById(faculty.getId());

    Assertions.assertEquals(faculty, testFaculty);
  }

  @Test
  public void getByIdThrowsEntityNotFound() {
    given(facultyRepository.findById(faculty.getId())).willReturn(Optional.empty());

    Assertions.assertThrows(EntityNotFoundException.class,
        () -> facultyService.getById(faculty.getId()));
  }
  //endregion

  //region GetByEmail tests
  @Test
  public void getByNameReturnsCorrectFaculty() {
    given(facultyRepository.findByName(faculty.getName())).willReturn(Optional.of(faculty));

    Faculty testFaculty = facultyService.getByName(faculty.getName());

    Assertions.assertEquals(faculty, testFaculty);
  }

  @Test
  public void getByNameThrowsEntityNotFound() {
    given(facultyRepository.findByName(faculty.getName())).willReturn(Optional.empty());

    Assertions.assertThrows(EntityNotFoundException.class,
        () -> facultyService.getByName(faculty.getName()));
  }
  //endregion

  //region GetFaculties tests
  @Test
  public void getFacultiesReturnsPagedFaculties() {
    Pageable pageable = Mockito.mock(PageRequest.class);
    Page<Faculty> mockPage = Mockito.mock(Page.class);

    given(facultyRepository.findAll(pageable)).willReturn(mockPage);

    Page<Faculty> faculties = facultyService.getFaculties(pageable);
    Assertions.assertEquals(faculties, mockPage);
  }
  //endregion

  //region GetDegreeByTitleAndFieldOfStudy tests
  @Test
  public void getDegreeByTitleAndFieldOfStudyReturnsCorrectDegree() {
    Degree degree = Degree.builder().fieldOfStudy("Computer Science").title(DegreeTitle.BACHELOR)
        .build();
    faculty.setDegrees(Collections.singletonList(degree));

    Degree testDegree = facultyService.getDegreeByTitleAndFieldOfStudy(faculty, degree.getTitle(),
        degree.getFieldOfStudy());

    Assertions.assertEquals(degree, testDegree);
  }

  @Test
  public void getDegreeByTitleAndFieldOfStudyThrowEntityNotFound() {
    Assertions.assertThrows(EntityNotFoundException.class,
        () -> facultyService.getDegreeByTitleAndFieldOfStudy(faculty, DegreeTitle.BACHELOR,
            "test"));
  }
  //endregion
}
