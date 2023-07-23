package com.leskiewicz.schoolsystem.degree.service;

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
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import io.jsonwebtoken.lang.Assert;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateDegreeTest {

  // Variables
  Degree degree;
  Faculty faculty;

  // Mocks
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
    degree = TestHelper.createDegree(faculty);
  }

  @Test
  public void createDegreeReturnsCreatedDegreeAndSavesIt() {
    // Create a mock CreateDegreeRequest
    CreateDegreeRequest request =
        CreateDegreeRequest.builder()
            .facultyName("Informatics")
            .title(DegreeTitle.BACHELOR)
            .fieldOfStudy("Computer Science")
            .build();

    // Mock the behavior of degreeRepository.findByFacultyNameAndTitleAndFieldOfStudy()
    given(
            degreeRepository.findByFacultyNameAndTitleAndFieldOfStudy(
                request.getFacultyName(), request.getTitle(), request.getFieldOfStudy()))
        .willReturn(Optional.empty());

    // Mock the behavior of facultyService.getByName()
    Faculty faculty = new Faculty();
    faculty.setName("Faculty Name");
    given(facultyService.getByName(request.getFacultyName())).willReturn(faculty);

    // Call the method to test
    DegreeDto degreeDto = Mockito.mock(DegreeDto.class);
    given(degreeMapper.convertToDto(any(Degree.class))).willReturn(degreeDto);

    DegreeDto result = degreeService.createDegree(request);

    // Assert the result
    Assert.notNull(result);

    // Verify the interactions with degreeRepository, degreeMapper, and facultyService
    verify(degreeRepository, times(1))
        .findByFacultyNameAndTitleAndFieldOfStudy(
            request.getFacultyName(), request.getTitle(), request.getFieldOfStudy());
    verify(facultyService, times(1)).getByName(request.getFacultyName());
    verify(degreeRepository, times(1)).save(any(Degree.class));
    verify(degreeMapper, times(1)).convertToDto(any(Degree.class));
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
    given(
            degreeRepository.findByFacultyNameAndTitleAndFieldOfStudy(
                request.getFacultyName(), request.getTitle(), request.getFieldOfStudy()))
        .willReturn(Optional.of(degree));

    Assertions.assertThrows(
        EntityAlreadyExistsException.class, () -> degreeService.createDegree(request));
  }
}
