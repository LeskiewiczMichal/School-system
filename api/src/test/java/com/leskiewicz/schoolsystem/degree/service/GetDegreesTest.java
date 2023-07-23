package com.leskiewicz.schoolsystem.degree.service;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.DegreeServiceImpl;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import io.jsonwebtoken.lang.Assert;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GetDegreesTest {

  // Mocks
  @Mock private DegreeRepository degreeRepository;
  @Mock private DegreeMapper degreeMapper;
  @InjectMocks private DegreeServiceImpl degreeService;

  @Test
  public void getDegreesReturnsPagedDegrees() {
    // Create some test data for Degree and DegreeDto
    List<Degree> degreeList = Arrays.asList(
            Degree.builder().id(1L).title(DegreeTitle.BACHELOR_OF_SCIENCE).build(),
            Degree.builder().id(2L).title(DegreeTitle.BACHELOR_OF_SCIENCE).build()
    );
    Page<Degree> degreesPage = new PageImpl<>(degreeList);

    // Mock the behavior of degreeRepository.findAll()
    given(degreeRepository.findAll(any(Pageable.class))).willReturn(degreesPage);

    // Mock the behavior of degreeMapper.convertToDto()
    List<DegreeDto> degreeDtoList = Arrays.asList(
            DegreeDto.builder().id(1L).title(DegreeTitle.BACHELOR_OF_SCIENCE).build(),
            DegreeDto.builder().id(2L).title(DegreeTitle.BACHELOR_OF_SCIENCE).build()
    );
    given(degreeMapper.convertToDto(any(Degree.class))).willReturn(degreeDtoList.get(0), degreeDtoList.get(1));

    // Call the method to test
    Page<DegreeDto> result = degreeService.getDegrees(PageRequest.of(0, 10));

    // Assert the result
    Assert.notNull(result);
    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(DegreeTitle.BACHELOR_OF_SCIENCE, result.getContent().get(0).getTitle());
    Assertions.assertEquals(DegreeTitle.BACHELOR_OF_SCIENCE, result.getContent().get(1).getTitle());

    // Verify the interactions with degreeRepository and degreeMapper
    verify(degreeRepository, times(1)).findAll(any(Pageable.class));
    verify(degreeMapper, times(2)).convertToDto(any(Degree.class));
  }
}
