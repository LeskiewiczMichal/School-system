package com.leskiewicz.schoolsystem.faculty.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.utils.StringUtils;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {

  // Variables
  Faculty faculty;
  // region Mocks
  @Mock private FacultyRepository facultyRepository;
  @Mock private FacultyMapper facultyMapper;
  @Mock private UserMapper userMapper;
  @Mock private DegreeMapper degreeMapper;
  // endregion
  @InjectMocks private FacultyServiceImpl facultyService;

  @BeforeEach
  public void setup() {

    // Set up test data
    faculty = Faculty.builder().id(1L).name("Software Engineering").build();
  }


  // region UpdateFaculty tests
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
  // endregion

  // region GetFacultyUsers tests
  @Test
  public void getFacultyUsersReturnsPagedUsers() {
    Faculty faculty = Mockito.mock(Faculty.class);
    Degree degree = Mockito.mock(Degree.class);
    List<User> userList = Arrays.asList(
            new User(1L, "John", "Doe", "john.doe@example.com", "12345", faculty, degree, Role.ROLE_STUDENT),
            new User(2L, "Jane", "Smith", "jane.smith@example.com", "12345", faculty, degree, Role.ROLE_STUDENT)
    );
    Page<User> usersPage = new PageImpl<>(userList);

    given(facultyRepository.findFacultyUsers(any(Long.class), any(Pageable.class), any(Role.class))).willReturn(usersPage);

    // Mock the behavior of the userMapper
    UserDto userDto1 = new UserDto(1L, "John", "Doe", "john.doe@example.com", "Some Faculty", "Some Degree", 1L);
    UserDto userDto2 = new UserDto(2L, "Jane", "Smith", "jane.smith@example.com", "Another Faculty", "Another Degree", 2L);
    given(userMapper.convertToDto(any(User.class))).willReturn(userDto1, userDto2);
    given(facultyRepository.existsById(any(Long.class))).willReturn(true);
    // Call the method to test
    Page<UserDto> result = facultyService.getFacultyUsers(1L, PageRequest.of(0, 10), Role.ROLE_STUDENT);

    // Assert the result
    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(userDto1, result.getContent().get(0));
    Assertions.assertEquals(userDto2, result.getContent().get(1));

    // Verify the interactions with userRepository and userMapper
    verify(facultyRepository, times(1)).findFacultyUsers(any(Long.class), any(Pageable.class), any(Role.class));
    verify(userMapper, times(2)).convertToDto(any(User.class));
  }

  @Test
  public void getFacultyUsersReturns404IfFacultyDoesntExist() {
    Pageable pageable = Mockito.mock(PageRequest.class);

    given(facultyRepository.existsById(any(Long.class))).willReturn(false);

    Assertions.assertThrows(
        EntityNotFoundException.class,
        () -> facultyService.getFacultyUsers(faculty.getId(), pageable, Role.ROLE_STUDENT));
  }
  // endregion

  //region GetFacultyDegrees tests
  @Test
  public void getFacultyDegreesReturnsPagedDegrees() {
    List<Degree> facultyList = Arrays.asList(
            Degree.builder().id(1L).title(DegreeTitle.BACHELOR).fieldOfStudy("Computer Science").faculty(faculty).build(),
            Degree.builder().id(2L).title(DegreeTitle.MASTER).fieldOfStudy("Computer Science").faculty(faculty).build()
    );
    Page<Degree> facultyPage = new PageImpl<>(facultyList);

    given(facultyRepository.findFacultyDegrees(any(Long.class), any(Pageable.class))).willReturn(facultyPage);

    // Mock the behavior of the userMapper
    DegreeDto degreeDto1 = new DegreeDto(1L, DegreeTitle.BACHELOR, "Computer Science", "Some faculty", 1L);
    DegreeDto degreeDto2 = new DegreeDto(2L, DegreeTitle.MASTER, "Computer Science", "Some faculty", 1L);


    given(degreeMapper.convertToDto(any(Degree.class))).willReturn(degreeDto1, degreeDto2);
    given(facultyRepository.existsById(any(Long.class))).willReturn(true);
    // Call the method to test
    Page<DegreeDto> result = facultyService.getFacultyDegrees(1L, PageRequest.of(0, 10));

    // Assert the result
    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(degreeDto1, result.getContent().get(0));
    Assertions.assertEquals(degreeDto2, result.getContent().get(1));

    // Verify the interactions with userRepository and userMapper
    verify(facultyRepository, times(1)).findFacultyDegrees(any(Long.class), any(Pageable.class));
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
  //endregion
}
