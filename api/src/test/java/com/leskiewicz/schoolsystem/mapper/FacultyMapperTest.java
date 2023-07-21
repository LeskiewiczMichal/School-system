package com.leskiewicz.schoolsystem.mapper;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapperImpl;
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
public class FacultyMapperTest {

  Faculty faculty;
  Degree degree;
  @InjectMocks
  private FacultyMapperImpl facultyMapper;

  @BeforeEach
  public void setup() {
    degree = Mockito.mock(Degree.class);

    faculty = TestHelper.createFaculty();
  }

  @Test
  public void convertToDtoCorrectForFaculty() {
    FacultyDto expectedFacultyDto = FacultyDto.builder().id(1L).name("TestFaculty").build();

    FacultyDto result = facultyMapper.convertToDto(faculty);

    Assertions.assertEquals(expectedFacultyDto, result);
  }



  @Test
  public void convertToDtoThrowsIllegalArgumentExceptionOnNoId() {
    Faculty testFaculty = faculty.toBuilder().id(null).build();

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> facultyMapper.convertToDto(testFaculty));
  }

  @Test
  public void throwsConstraintViolationExceptionOnInvalidFaculty() {
    Faculty testFaculty = faculty.toBuilder().name(null).build();

    Assertions.assertThrows(ConstraintViolationException.class,
        () -> facultyMapper.convertToDto(testFaculty));
  }

}
