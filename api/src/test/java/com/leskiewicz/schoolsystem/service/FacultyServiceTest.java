package com.leskiewicz.schoolsystem.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
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
  // endregion
  @InjectMocks private FacultyServiceImpl facultyService;

  @BeforeEach
  public void setup() {

    // Set up test data
    faculty = Faculty.builder().id(1L).name("Software Engineering").build();
  }

  // region GetById tests
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
  // endregion

  // region GetByEmail tests
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
  // endregion

  // region GetFaculties tests
  @Test
  public void getFacultiesReturnsPagedFaculties() {
    Degree degree = Mockito.mock(Degree.class);
    List<Faculty> facultyList = Arrays.asList(
          Faculty.builder().id(1L).name("Software Engineering").degrees(Collections.singletonList(degree)).build(),
            Faculty.builder().id(2L).name("Computer Science").degrees(Collections.singletonList(degree)).build()

    );
    Page<Faculty> usersPage = new PageImpl<>(facultyList);

    given(facultyRepository.findAll(any(Pageable.class))).willReturn(usersPage);

    // Mock the behavior of the userMapper
    FacultyDto facultyDto1 = new FacultyDto(1L, "Software Engineering");
    FacultyDto facultyDto2 = new FacultyDto(2L, "Computer Science");
    given(facultyMapper.convertToDto(any(Faculty.class))).willReturn(facultyDto1, facultyDto2);

    // Call the method to test
    Page<FacultyDto> result = facultyService.getFaculties(PageRequest.of(0, 10));

    // Assert the result
    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(facultyDto1, result.getContent().get(0));
    Assertions.assertEquals(facultyDto2, result.getContent().get(1));

    // Verify the interactions with userRepository and userMapper
    verify(facultyRepository, times(1)).findAll(any(Pageable.class));
    verify(facultyMapper, times(2)).convertToDto(any(Faculty.class));
  }
  // endregion

  // region GetDegreeByTitleAndFieldOfStudy tests
  @Test
  public void getDegreeByTitleAndFieldOfStudyReturnsCorrectDegree() {
    Degree degree =
        Degree.builder().fieldOfStudy("Computer Science").title(DegreeTitle.BACHELOR).build();
    faculty.setDegrees(Collections.singletonList(degree));

    Degree testDegree =
        facultyService.getDegreeByTitleAndFieldOfStudy(
            faculty, degree.getTitle(), degree.getFieldOfStudy());

    Assertions.assertEquals(degree, testDegree);
  }

  @Test
  public void getDegreeByTitleAndFieldOfStudyThrowEntityNotFound() {
    Assertions.assertThrows(
        EntityNotFoundException.class,
        () ->
            facultyService.getDegreeByTitleAndFieldOfStudy(faculty, DegreeTitle.BACHELOR, "test"));
  }
  // endregion

  // region CreateFaculty tests
  @Test
  public void createsAndReturnsFacultyAndOnProperRequest() {
    // Create a mock request
    CreateFacultyRequest request = new CreateFacultyRequest();
    request.setName("Faculty Name");

    // Mock the behavior of facultyRepository.findByName()
    given(facultyRepository.findByName(request.getName())).willReturn(Optional.empty());

    // Mock the behavior of facultyMapper.convertToDto()
    Faculty faculty = new Faculty();
    faculty.setName(StringUtils.capitalizeFirstLetterOfEveryWord(request.getName()));
    given(facultyMapper.convertToDto(any(Faculty.class))).willReturn(new FacultyDto(1L, faculty.getName()));

    // Call the method to test
    FacultyDto createdFaculty = facultyService.createFaculty(request);

    // Assert the result
    Assert.notNull(createdFaculty);
    Assertions.assertEquals("Faculty Name", createdFaculty.getName());

    // Verify the interactions with facultyRepository and facultyMapper
    verify(facultyRepository, times(1)).findByName(request.getName());
    verify(facultyRepository, times(1)).save(any(Faculty.class));
    verify(facultyMapper, times(1)).convertToDto(any(Faculty.class));
  }

  @Test
  public void throwsConstraintViolationExceptionOnRequestInvalid() {
    CreateFacultyRequest request = new CreateFacultyRequest(null);

    Assertions.assertThrows(
        ConstraintViolationException.class, () -> facultyService.createFaculty(request));
  }

  @Test
  public void throwsEntityAlreadyExistsExceptionOnNameThatIsAlreadyTaken() {
    CreateFacultyRequest request = new CreateFacultyRequest(faculty.getName());

    given(facultyRepository.findByName(any(String.class))).willReturn(Optional.of(faculty));

    Assertions.assertThrows(
        EntityAlreadyExistsException.class, () -> facultyService.createFaculty(request));
  }
  // endregion

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
}
