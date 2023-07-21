package com.leskiewicz.schoolsystem.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.DegreeServiceImpl;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class DegreeServiceTest {

  // Variables
  Degree degree;
  Faculty faculty;

  @Mock private DegreeRepository degreeRepository;
  @Mock private FacultyServiceImpl facultyService;

  @InjectMocks private DegreeServiceImpl degreeService;

  @BeforeEach
  public void setup() {
    faculty = Mockito.mock(Faculty.class);

    // Set up test data
    degree =
        Degree.builder()
            .id(1L)
            .faculty(faculty)
            .title(DegreeTitle.BACHELOR_OF_SCIENCE)
            .fieldOfStudy("Computer Science")
            .build();
  }

  // region GetById tests
  @Test
  public void getByIdReturnsCorrectDegree() {
    given(degreeRepository.findById(degree.getId())).willReturn(Optional.of(degree));

    Degree testDegree = degreeService.getById(degree.getId());

    Assertions.assertEquals(degree, testDegree);
  }

  @Test
  public void getByIdThrowsEntityNotFoundExceptionWhenDegreeDoesNotExist() {
    given(degreeRepository.findById(degree.getId())).willReturn(Optional.empty());

    Assertions.assertThrows(
        EntityNotFoundException.class, () -> degreeService.getById(degree.getId()));
  }

  // endregion

  // region GetDegrees tests
  @Test
  public void getDegreesReturnsPagedDegrees() {
    Pageable pageable = Mockito.mock(PageRequest.class);
    Page<Degree> mockPage = Mockito.mock(Page.class);

    given(degreeRepository.findAll(pageable)).willReturn(mockPage);

    Page<Degree> degrees = degreeService.getDegrees(pageable);
    Assertions.assertEquals(mockPage, degrees);
  }
  // endregion

  // region GetByTitleAndFieldOfStudy tests
  @Test
  public void getByTitleAndFieldOfStudyReturnsCorrectDegree() {
    given(degreeRepository.findByTitleAndFieldOfStudy(degree.getTitle(), degree.getFieldOfStudy()))
        .willReturn(Optional.of(degree));

    Degree testDegree =
        degreeService.getByTitleAndFieldOfStudy(degree.getTitle(), degree.getFieldOfStudy());

    Assertions.assertEquals(degree, testDegree);
  }

  @Test
  public void getByTitleAndFieldOfStudyThrowsEntityNotFoundExceptionWhenDegreeDoesNotExist() {
    given(degreeRepository.findByTitleAndFieldOfStudy(degree.getTitle(), degree.getFieldOfStudy()))
        .willReturn(Optional.empty());

    Assertions.assertThrows(
        EntityNotFoundException.class,
        () -> degreeService.getByTitleAndFieldOfStudy(degree.getTitle(), degree.getFieldOfStudy()));
  }
  // endregion

  // region CreateDegree tests
  @Test
  public void createDegreeReturnsCreatedDegree() {
    CreateDegreeRequest request =
        new CreateDegreeRequest(
            DegreeTitle.BACHELOR_OF_SCIENCE, "Computer Science", "Software Engineering");
    given(facultyService.getByName(any(String.class))).willReturn(faculty);

    Degree testDegree = degreeService.createDegree(request);

    // Responded with proper degree
    Assertions.assertEquals(degree.getFieldOfStudy(), testDegree.getFieldOfStudy());

    // Proper degree was saved
    ArgumentCaptor<Degree> argumentCaptor = ArgumentCaptor.forClass(Degree.class);
    verify(degreeRepository).save(argumentCaptor.capture());
    Degree capturedDegree = argumentCaptor.getValue();
    Assertions.assertEquals(degree.getFieldOfStudy(), capturedDegree.getFieldOfStudy());
  }
  // region
}
