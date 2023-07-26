package com.leskiewicz.schoolsystem.degree.service;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.DegreeServiceImpl;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DegreeServiceGettersTest {

  // Mocks
  @Mock private DegreeRepository degreeRepository;
  @Mock private DegreeMapper degreeMapper;

  @InjectMocks private DegreeServiceImpl degreeService;

  Degree degree;
  Faculty faculty;

  @BeforeEach
  public void setup() {
    faculty = Mockito.mock(Faculty.class);
    degree = TestHelper.createDegree(faculty);
  }

  @Test
  public void getDegreesReturnsPagedDegrees() {
    // Set up test data
    List<Degree> degreeList =
        Arrays.asList(
            Degree.builder().id(1L).title(DegreeTitle.BACHELOR_OF_SCIENCE).build(),
            Degree.builder().id(2L).title(DegreeTitle.BACHELOR_OF_SCIENCE).build());
    List<DegreeDto> degreeDtoList =
        Arrays.asList(
            DegreeDto.builder().id(1L).title(DegreeTitle.BACHELOR_OF_SCIENCE).build(),
            DegreeDto.builder().id(2L).title(DegreeTitle.BACHELOR_OF_SCIENCE).build());

    CommonTests.serviceGetAll(
        Degree.class,
        degreeList,
        degreeDtoList,
        degreeRepository::findAll,
        degreeMapper::convertToDto,
        degreeService::getDegrees);
  }

  /// *** GetById *** ///
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

  @Test
  public void getDegreeCourses() {}
}
