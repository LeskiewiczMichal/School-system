package com.leskiewicz.schoolsystem.faculty.service;

import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GetFacultyByTests {

    // Mocks
    @Mock
    private FacultyRepository facultyRepository;
    @Mock private FacultyMapper facultyMapper;
    @InjectMocks
    private FacultyServiceImpl facultyService;

    Faculty faculty;

    @BeforeEach
    public void setup() {

        // Set up test data
        faculty = Faculty.builder().id(1L).name("Software Engineering").build();
    }

    @Test
    public void getByIdReturnsCorrectFaculty() {
        // Create a mock Faculty object and FacultyDto
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Faculty Name");

        FacultyDto facultyDto = new FacultyDto(1L, "Faculty Name");

        // Mock the behavior of facultyRepository.findById() to return the mock Faculty
        given(facultyRepository.findById(1L)).willReturn(Optional.of(faculty));

        // Mock the behavior of facultyMapper.convertToDto()
        given(facultyMapper.convertToDto(any(Faculty.class))).willReturn(facultyDto);

        // Call the method to test
        FacultyDto result = facultyService.getById(1L);

        // Assert the result
        Assert.notNull(result);
        Assertions.assertEquals("Faculty Name", result.getName());

        // Verify the interactions with facultyRepository and facultyMapper
        verify(facultyRepository, times(1)).findById(1L);
        verify(facultyMapper, times(1)).convertToDto(any(Faculty.class));
    }

    @Test
    public void getByIdThrowsEntityNotFound() {
        given(facultyRepository.findById(faculty.getId())).willReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class, () -> facultyService.getById(faculty.getId()));
    }

    @Test
    public void getByNameReturnsCorrectFaculty() {
        given(facultyRepository.findByName(faculty.getName())).willReturn(Optional.of(faculty));

        Faculty testFaculty = facultyService.getByName(faculty.getName());

        Assertions.assertEquals(faculty, testFaculty);
    }

    @Test
    public void getByNameThrowsEntityNotFound() {
        given(facultyRepository.findByName(faculty.getName())).willReturn(Optional.empty());

        Assertions.assertThrows(
                EntityNotFoundException.class, () -> facultyService.getByName(faculty.getName()));
    }
}
