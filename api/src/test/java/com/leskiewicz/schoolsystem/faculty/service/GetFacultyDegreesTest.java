package com.leskiewicz.schoolsystem.faculty.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
public class GetFacultyDegreesTest {

  // Variables
  Faculty faculty;

  // Mocks
  @Mock private FacultyRepository facultyRepository;
  @Mock private DegreeRepository degreeRepository;
  @Mock private DegreeMapper degreeMapper;
  @InjectMocks private FacultyServiceImpl facultyService;

  @BeforeEach
  public void setup() {

    // Set up test data
    faculty = Faculty.builder().id(1L).name("Software Engineering").build();
  }

  @Test
  public void getFacultyDegreesReturnsPagedDegrees() {
    List<Degree> facultyList =
        Arrays.asList(
            Degree.builder()
                .id(1L)
                .title(DegreeTitle.BACHELOR)
                .fieldOfStudy("Computer Science")
                .faculty(faculty)
                .build(),
            Degree.builder()
                .id(2L)
                .title(DegreeTitle.MASTER)
                .fieldOfStudy("Computer Science")
                .faculty(faculty)
                .build());
    Page<Degree> facultyPage = new PageImpl<>(facultyList);

    given(degreeRepository.findDegreesByFacultyId(any(Long.class), any(Pageable.class)))
        .willReturn(facultyPage);

    // Mock the behavior of the userMapper
    DegreeDto degreeDto1 =
        new DegreeDto(1L, DegreeTitle.BACHELOR, "Computer Science", "Some faculty", 1L);
    DegreeDto degreeDto2 =
        new DegreeDto(2L, DegreeTitle.MASTER, "Computer Science", "Some faculty", 1L);

    given(degreeMapper.convertToDto(any(Degree.class))).willReturn(degreeDto1, degreeDto2);
    given(facultyRepository.existsById(any(Long.class))).willReturn(true);
    // Call the method to test
    Page<DegreeDto> result = facultyService.getFacultyDegrees(1L, PageRequest.of(0, 10));

    // Assert the result
    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(degreeDto1, result.getContent().get(0));
    Assertions.assertEquals(degreeDto2, result.getContent().get(1));

    // Verify the interactions with userRepository and userMapper
    verify(degreeRepository, times(1)).findDegreesByFacultyId(any(Long.class), any(Pageable.class));
    verify(degreeMapper, times(2)).convertToDto(any(Degree.class));
  }

  @Test
  public void getFacultyDegreesReturns404IfFacultyDoesntExist() {
    Pageable pageable = Mockito.mock(PageRequest.class);

    given(facultyRepository.existsById(any(Long.class))).willReturn(false);

    Assertions.assertThrows(
        EntityNotFoundException.class,
        () -> facultyService.getFacultyDegrees(faculty.getId(), pageable));
  }
}
