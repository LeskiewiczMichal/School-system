package com.leskiewicz.schoolsystem.mapper;

import static org.mockito.BDDMockito.given;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapperImpl;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DegreeMapperTest {

  @InjectMocks
  private DegreeMapperImpl degreeMapper;

  // Variables
  Degree degree;
  Faculty faculty;

  @BeforeEach
  public void setup() {
    faculty = Mockito.mock(Faculty.class);
    degree = TestHelper.createDegree(faculty);

    given(faculty.getName()).willReturn("DegreeTitle");
  }

  @Test
  public void convertToDtoCorrectForDegree() {
    DegreeDto expectedDegreeDto = DegreeDto.builder()
        .id(0L)
        .fieldOfStudy("Computer Science")
        .title(DegreeTitle.BACHELOR_OF_SCIENCE)
        .faculty("DegreeTitle")
        .build();

    DegreeDto result = degreeMapper.convertToDto(degree);

    Assertions.assertEquals(expectedDegreeDto, result);
  }

//  @Test



}
