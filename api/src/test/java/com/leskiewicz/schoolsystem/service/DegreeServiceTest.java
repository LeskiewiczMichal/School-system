package com.leskiewicz.schoolsystem.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.DegreeServiceImpl;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.CreateDegreeRequest;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
  @Mock private DegreeMapper degreeMapper;

  @InjectMocks private DegreeServiceImpl degreeService;

  static Stream<Arguments> throwsConstraintViolationExceptionOnRequestInvalidProvider() {
    return Stream.of(
        Arguments.of(new CreateDegreeRequest(null, "Computer Science", "Software Engineering")),
        Arguments.of(
            new CreateDegreeRequest(DegreeTitle.BACHELOR_OF_SCIENCE, null, "Software Engineering")),
        Arguments.of(
            new CreateDegreeRequest(DegreeTitle.BACHELOR_OF_SCIENCE, "Computer Science", null)),
        Arguments.of(new CreateDegreeRequest(null, null, null)));
  }

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

  // endregion

  // region GetById tests
  @Test
  public void getByIdReturnsCorrectDegree() {
    // Create a mock Degree object and DegreeDto
    Degree degree = new Degree();
    degree.setId(1L);
    degree.setTitle(DegreeTitle.BACHELOR_OF_SCIENCE);

    DegreeDto degreeDto = DegreeDto.builder().id(1L).title(DegreeTitle.BACHELOR_OF_SCIENCE).build();


    // Mock the behavior of degreeRepository.findById() to return the mock Degree
    given(degreeRepository.findById(1L)).willReturn(Optional.of(degree));

    // Mock the behavior of degreeMapper.convertToDto()
    given(degreeMapper.convertToDto(any(Degree.class))).willReturn(degreeDto);

    // Call the method to test
    DegreeDto result = degreeService.getById(1L);

    // Assert the result
    Assert.notNull(result);
    Assertions.assertEquals(DegreeTitle.BACHELOR_OF_SCIENCE, result.getTitle());

    // Verify the interactions with degreeRepository and degreeMapper
    verify(degreeRepository, times(1)).findById(1L);
    verify(degreeMapper, times(1)).convertToDto(any(Degree.class));
  }
  // endregion

  @Test
  public void getByIdThrowsEntityNotFoundExceptionWhenDegreeDoesNotExist() {
    given(degreeRepository.findById(degree.getId())).willReturn(Optional.empty());

    Assertions.assertThrows(
        EntityNotFoundException.class, () -> degreeService.getById(degree.getId()));
  }

  // region GetDegrees tests
  @Test
  public void getDegreesReturnsPagedDegrees() {
    Pageable pageable = Mockito.mock(PageRequest.class);
    Page<Degree> mockPage = Mockito.mock(Page.class);

    given(degreeRepository.findAll(pageable)).willReturn(mockPage);

    Page<DegreeDto> degrees = degreeService.getDegrees(pageable);
    Assertions.assertEquals(mockPage, degrees);
  }
  // endregion

  // region GetByTitleAndFieldOfStudy tests
  @Test
  public void getByTitleAndFieldOfStudyReturnsCorrectDegree() {
    given(degreeRepository.findByTitleAndFieldOfStudy(degree.getTitle(), degree.getFieldOfStudy()))
        .willReturn(List.of(degree, Mockito.mock(Degree.class)));

    List<Degree> testDegree =
        degreeService.getByTitleAndFieldOfStudy(degree.getTitle(), degree.getFieldOfStudy());

    Assertions.assertEquals(2, testDegree.size());
    Assertions.assertEquals(degree, testDegree.get(0));
  }

  // region CreateDegree tests
  @Test
  public void createDegreeReturnsCreatedDegreeAndSavesIt() {
    CreateDegreeRequest request =
        new CreateDegreeRequest(
            DegreeTitle.BACHELOR_OF_SCIENCE, "Computer Science", "Software Engineering");
    given(facultyService.getByName(any(String.class))).willReturn(faculty);

    DegreeDto testDegree = degreeService.createDegree(request);

    // Responded with proper degree
    Assertions.assertEquals(degree.getFieldOfStudy(), testDegree.getFieldOfStudy());

    // Proper degree was saved
    ArgumentCaptor<Degree> argumentCaptor = ArgumentCaptor.forClass(Degree.class);
    verify(degreeRepository).save(argumentCaptor.capture());
    Degree capturedDegree = argumentCaptor.getValue();
    Assertions.assertEquals(degree.getFieldOfStudy(), capturedDegree.getFieldOfStudy());
  }

  @ParameterizedTest
  @MethodSource("throwsConstraintViolationExceptionOnRequestInvalidProvider")
  public void throwsConstraintViolationExceptionOnRequestInvalid(CreateDegreeRequest request) {
    Assertions.assertThrows(
        ConstraintViolationException.class, () -> degreeService.createDegree(request));
  }

  @Test
    public void throwsEntityAlreadyExistsExceptionWhenDegreeAlreadyExists() {
        CreateDegreeRequest request =
            new CreateDegreeRequest(
                DegreeTitle.BACHELOR_OF_SCIENCE, "Computer Science", "Software Engineering");
        given(degreeRepository
                .findByFacultyNameAndTitleAndFieldOfStudy(
                    request.getFacultyName(), request.getTitle(), request.getFieldOfStudy())
                ).willReturn(Optional.of(degree));

        Assertions.assertThrows(
            EntityAlreadyExistsException.class, () -> degreeService.createDegree(request));
    }
  // region
}
