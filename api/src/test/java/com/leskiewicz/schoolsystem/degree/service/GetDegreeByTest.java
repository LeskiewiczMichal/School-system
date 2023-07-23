package com.leskiewicz.schoolsystem.degree.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.DegreeServiceImpl;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityNotFoundException;
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

@ExtendWith(MockitoExtension.class)
public class GetDegreeByTest {

  // Variables
  Degree degree;
  Faculty faculty;

  // Mocks
  @Mock private DegreeRepository degreeRepository;
  @Mock private DegreeMapper degreeMapper;

  @InjectMocks private DegreeServiceImpl degreeService;

  @BeforeEach
  public void setup() {
    faculty = Mockito.mock(Faculty.class);
    degree = TestHelper.createDegree(faculty);
  }

  // *** GetById ***//

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

  @Test
  public void getByIdThrowsEntityNotFoundExceptionWhenDegreeDoesNotExist() {
    given(degreeRepository.findById(degree.getId())).willReturn(Optional.empty());

    Assertions.assertThrows(
        EntityNotFoundException.class, () -> degreeService.getById(degree.getId()));
  }

  // *** GetByTitleAndFieldOfStudy ***//
  @Test
  public void getByTitleAndFieldOfStudyReturnsCorrectDegree() {
    given(degreeRepository.findByTitleAndFieldOfStudy(degree.getTitle(), degree.getFieldOfStudy()))
        .willReturn(List.of(degree, Mockito.mock(Degree.class)));

    List<Degree> testDegree =
        degreeService.getByTitleAndFieldOfStudy(degree.getTitle(), degree.getFieldOfStudy());

    Assertions.assertEquals(2, testDegree.size());
    Assertions.assertEquals(degree, testDegree.get(0));
  }
}
