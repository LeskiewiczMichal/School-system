package com.leskiewicz.schoolsystem.service;

import static org.mockito.BDDMockito.given;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.DegreeServiceImpl;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import jakarta.persistence.EntityNotFoundException;

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

@ExtendWith(MockitoExtension.class)
public class DegreeServiceTest {

  // Variables
  Degree degree;
  Faculty faculty;

  @Mock private DegreeRepository degreeRepository;

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

  //region GetDegrees tests
  @Test
  public void getDegreesReturnsPagedDegrees() {
    Pageable pageable = Mockito.mock(PageRequest.class);
    Page<Degree> mockPage = Mockito.mock(Page.class);

    given(degreeRepository.findAll(pageable)).willReturn(mockPage);

    Page<Degree> degrees = degreeService.getDegrees(pageable);
    Assertions.assertEquals(mockPage, degrees);
  }
  //endregion

    //region GetByTitleAndFieldOfStudy tests
    @Test
    public void getByTitleAndFieldOfStudyReturnsCorrectDegree() {
      given(degreeRepository.findByTitleAndFieldOfStudy(degree.getTitle(), degree.getFieldOfStudy()))
          .willReturn(Optional.of(degree));

      Degree testDegree =
          degreeService.getByTitleAndFieldOfStudy(degree.getTitle(), degree.getFieldOfStudy());

      Assertions.assertEquals(degree, testDegree);
    }

    
    //endregion
}
