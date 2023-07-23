package com.leskiewicz.schoolsystem.mapper;

import static org.mockito.BDDMockito.given;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapperImpl;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DegreeMapperTest {

  // Variables
  Degree degree;
  Faculty faculty;
  @InjectMocks private DegreeMapperImpl degreeMapper;

  @BeforeEach
  public void setup() {
    faculty = Mockito.mock(Faculty.class);
    degree = TestHelper.createDegree(faculty);
  }

  @Test
  public void convertToDtoCorrectForDegree() {
    // Setup
    DegreeDto expectedDegreeDto =
        DegreeDto.builder()
            .id(0L)
            .fieldOfStudy("Computer Science")
            .title(DegreeTitle.BACHELOR_OF_SCIENCE)
            .faculty("DegreeTitle")
            .facultyId(0L)
            .build();

    // Mocking
    given(faculty.getName()).willReturn("DegreeTitle");
    given(faculty.getId()).willReturn(0L);

    // Call function
    DegreeDto result = degreeMapper.convertToDto(degree);

    // Assertions
    Assertions.assertEquals(expectedDegreeDto, result);
  }

  @Test
  public void convertToDtoThrowsIllegalArgumentExceptionOnNoId() {
    degree.setId(null);

    Assertions.assertThrows(
        IllegalArgumentException.class, () -> degreeMapper.convertToDto(degree));
  }

  @Test
  public void throwsConstraintViolationExceptionOnInvalidDegree() {
    degree.setFieldOfStudy(null);

    Assertions.assertThrows(
        ConstraintViolationException.class, () -> degreeMapper.convertToDto(degree));
  }
}
