package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyModelAssembler;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
  //region

}
