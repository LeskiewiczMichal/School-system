package com.leskiewicz.schoolsystem.faculty.service;

import static com.leskiewicz.schoolsystem.builders.FacultyBuilder.aFaculty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import com.leskiewicz.schoolsystem.utils.Mapper;
import com.leskiewicz.schoolsystem.utils.StringUtils;
import io.jsonwebtoken.lang.Assert;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateFacultyTest {

  Faculty faculty;
  // Mocks
  @Mock private FacultyRepository facultyRepository;
  @Mock private Mapper<Faculty, FacultyDto> facultyMapper;
  @InjectMocks private FacultyServiceImpl facultyService;

  @BeforeEach
  public void setup() {

    // Set up test data
    faculty = Faculty.builder().id(1L).name("Software Engineering").build();
  }

  @Test
  public void createsAndReturnsFacultyAndOnProperRequest() {
    // Create a mock request
    CreateFacultyRequest request = new CreateFacultyRequest("name");

    // Mock the behavior of facultyRepository.findByName()
    given(facultyRepository.findByName(request.name())).willReturn(Optional.empty());
  given(facultyRepository.save(any(Faculty.class))).willReturn(aFaculty().build());

    // Mock the behavior of facultyMapper.convertToDto()
    Faculty faculty = new Faculty();
    faculty.setName(StringUtils.capitalizeFirstLetterOfEveryWord(request.name()));
    given(facultyMapper.mapToDto(any(Faculty.class)))
        .willReturn(new FacultyDto(1L, faculty.getName()));

    // Call the method to test
    FacultyDto createdFaculty = facultyService.createFaculty(request);

    // Assert the result
    Assert.notNull(createdFaculty);
    Assertions.assertEquals("Faculty Name", createdFaculty.getName());

    // Verify the interactions with facultyRepository and facultyMapper
    verify(facultyRepository, times(1)).findByName(request.name());
    verify(facultyRepository, times(1)).save(any(Faculty.class));
    verify(facultyMapper, times(1)).mapToDto(any(Faculty.class));
  }
}
