package com.leskiewicz.schoolsystem.faculty.service;

import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UpdateFacultyTest {

    // Mocks
    @Mock
    private FacultyRepository facultyRepository;
    @Mock private FacultyMapper facultyMapper;
    @InjectMocks
    private FacultyServiceImpl facultyService;

    // Variables
    Faculty faculty;

    @BeforeEach
    public void setup() {

        // Set up test data
        faculty = Faculty.builder().id(1L).name("Software Engineering").build();
    }

    @Test
    public void updateFacultySavesCorrectFaculty() {
        given(facultyRepository.findById(any(Long.class))).willReturn(Optional.of(faculty));
        PatchFacultyRequest request = new PatchFacultyRequest("new name");

        FacultyDto mockDto = Mockito.mock(FacultyDto.class);
        given(facultyMapper.convertToDto(any(Faculty.class))).willReturn(mockDto);

        FacultyDto result = facultyService.updateFaculty(request, faculty.getId());

        faculty.setName("New name");

        verify(facultyRepository).save(faculty);
        Assertions.assertEquals(mockDto, result);
    }

    @Test
    public void updateFacultyThrowsEntityNotFound() {
        given(facultyRepository.findById(any(Long.class))).willReturn(Optional.empty());
        PatchFacultyRequest request = new PatchFacultyRequest("new name");

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> facultyService.updateFaculty(request, faculty.getId()));
    }

    @Test
    public void updateFacultyThrowsEntityAlreadyExistsException() {
        given(facultyRepository.findById(any(Long.class))).willReturn(Optional.of(faculty));
        PatchFacultyRequest request = new PatchFacultyRequest("new name");
        Faculty faculty2 = Faculty.builder().id(2L).name("new name").build();
        given(facultyRepository.findByName(any(String.class))).willReturn(Optional.of(faculty2));

        Assertions.assertThrows(
                EntityAlreadyExistsException.class,
                () -> facultyService.updateFaculty(request, faculty.getId()));
    }
}
