package com.leskiewicz.schoolsystem.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeService;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.error.customexception.UserAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.UserServiceImpl;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  // Variables
  Faculty faculty;
  Degree degree;
  User user;
  //region Mocks
  @Mock
  private UserRepository userRepository;
  @Mock
  private DegreeService degreeService;
  //endregion
  @Mock
  private FacultyService facultyService;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private UserMapper userMapper;
  @InjectMocks
  private UserServiceImpl userService;

  static Stream<Arguments> updateUserWithDifferentFieldsSavesProperUserProvider() {
    Faculty baseFaculty = Faculty.builder().name("Engineering").build();

    Degree baseDegree = Degree.builder().title(DegreeTitle.BACHELOR)
        .fieldOfStudy("Computer Science").faculty(baseFaculty).build();

    User baseUser = User.builder().email("test@example.com").firstName("Tester").lastName("Testing")
        .password("encoded_password").role(Role.ROLE_STUDENT).faculty(baseFaculty)
        .degree(baseDegree).build();

    return Stream.of(Arguments.of(PatchUserRequest.builder().firstName("Test").build(),
            baseUser.toBuilder().build(), baseUser.toBuilder().firstName("Test").build()),
        Arguments.of(PatchUserRequest.builder().lastName("Test").build(),
            baseUser.toBuilder().build(), baseUser.toBuilder().lastName("Test").build()),
        Arguments.of(PatchUserRequest.builder().email("test@example.com").build(),
            baseUser.toBuilder().build(), baseUser.toBuilder().email("test@example.com").build()),
        Arguments.of(PatchUserRequest.builder().password("Test").build(),
            baseUser.toBuilder().build(), baseUser.toBuilder().password("encoded").build()));
  }

  @BeforeEach
  public void setUp() {
    faculty = Mockito.mock(Faculty.class);
    degree = Mockito.mock(Degree.class);

    user = TestHelper.createUser(faculty, degree);
  }

  //region GetById tests
  @Test
  public void getByIdHappyPath() {
    UserDto dto = Mockito.mock(UserDto.class);
    given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
    given(userMapper.convertToDto(user)).willReturn(dto);

    UserDto testUser = userService.getById(1L);

    Assertions.assertEquals(dto, testUser);
  }
  //endregion

  @Test
  public void getByIdThrowsEntityNotFound() {
    given(userRepository.findById(any(Long.class))).willReturn(Optional.empty());

    Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getById(1L));
  }

  //region GetByEmail tests
  @Test
  public void getByEmailHappyPath() {
    given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

    User testUser = userService.getByEmail("email@example.com");

    Assertions.assertEquals(user, testUser);
  }
  //endregion

  @Test
  public void getByEmailThrowsEntityNotFound() {
    given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

    Assertions.assertThrows(EntityNotFoundException.class,
        () -> userService.getByEmail("email@example/com"));
  }
  //endregion

  //region GetUsers tests
  @Test
  public void getUsersReturnsPagedUsers() {
    Faculty faculty = Mockito.mock(Faculty.class);
    Degree degree = Mockito.mock(Degree.class);
    List<User> userList = Arrays.asList(
            new User(1L, "John", "Doe", "john.doe@example.com", "12345", faculty, degree, Role.ROLE_STUDENT),
            new User(2L, "Jane", "Smith", "jane.smith@example.com", "12345", faculty, degree, Role.ROLE_ADMIN)
    );
    Page<User> usersPage = new PageImpl<>(userList);

    given(userRepository.findAll(any(Pageable.class))).willReturn(usersPage);

    // Mock the behavior of the userMapper
    UserDto userDto1 = new UserDto(1L, "John", "Doe", "john.doe@example.com", "Some Faculty", "Some Degree", 1L);
    UserDto userDto2 = new UserDto(2L, "Jane", "Smith", "jane.smith@example.com", "Another Faculty", "Another Degree", 2L);
    given(userMapper.convertToDto(any(User.class))).willReturn(userDto1, userDto2);

    // Call the method to test
    Page<UserDto> result = userService.getUsers(PageRequest.of(0, 10));

    // Assert the result
   Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(userDto1, result.getContent().get(0));
    Assertions.assertEquals(userDto2, result.getContent().get(1));

    // Verify the interactions with userRepository and userMapper
    verify(userRepository, times(1)).findAll(any(Pageable.class));
    verify(userMapper, times(2)).convertToDto(any(User.class));
  }

  //region AddUser tests
  @Test
  public void addUserSavesUser() {
    given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

    userService.addUser(user);

    verify(userRepository).save(user);
  }
  //endregion

  @Test
  public void throwsUserAlreadyExistsExceptionWhenUserWithGivenEmailAlreadyExists() {
    given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

    Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(user));
  }

  //region UpdateUser Tests
  @ParameterizedTest
  @MethodSource("updateUserWithDifferentFieldsSavesProperUserProvider")
  public void updateUserWithDifferentFieldsSavesProperUser(PatchUserRequest request, User baseUser,
      User expectedUserToSave) {
    given(userRepository.findById(baseUser.getId())).willReturn(Optional.of(baseUser));
    Mockito.lenient().when(passwordEncoder.encode(any(String.class))).thenReturn("encoded");
    Mockito.lenient().when(userRepository.findByEmail(any(String.class)))
        .thenReturn(Optional.empty());

    userService.updateUser(request, baseUser.getId());

    verify(userRepository).save(expectedUserToSave);
  }

  @Test
  public void updateUserDegree() {
    Degree testDegree = Degree.builder().fieldOfStudy("Testing").title(DegreeTitle.BACHELOR)
        .faculty(faculty).build();
    User testUser = user.toBuilder().build();
    User expectedUser = user.toBuilder().degree(testDegree).build();

    given(facultyService.getDegreeByTitleAndFieldOfStudy(any(Faculty.class), any(DegreeTitle.class),
        any(String.class))).willReturn(testDegree);
    given(userRepository.findById(testUser.getId())).willReturn(Optional.of(testUser));

    userService.updateUser(PatchUserRequest.builder().degreeTitle(testDegree.getTitle())
        .degreeField(testDegree.getFieldOfStudy()).build(), testUser.getId());

    verify(userRepository).save(expectedUser);
  }

  @Test
  public void updateUserFaculty() {
    Faculty testFaculty = Faculty.builder().name("test").degree(degree).build();
    User testUser = user.toBuilder().build();
    User expectedUser = user.toBuilder().faculty(testFaculty).build();

    given(degree.getFieldOfStudy()).willReturn("mock");
    given(degree.getTitle()).willReturn(DegreeTitle.BACHELOR);
    given(facultyService.getDegreeByTitleAndFieldOfStudy(any(Faculty.class), any(DegreeTitle.class),
        any(String.class))).willReturn(degree);
    given(facultyService.getByName(any(String.class))).willReturn(testFaculty);
    given(userRepository.findById(testUser.getId())).willReturn(Optional.of(testUser));

    userService.updateUser(PatchUserRequest.builder().facultyName("test").build(),
        testUser.getId());

    verify(userRepository).save(expectedUser);
  }

  @Test
  public void updateUserFacultyAndDegree() {
    Faculty testFaculty = Faculty.builder().name("test").build();
    Degree testDegree = Degree.builder().fieldOfStudy("Testing").title(DegreeTitle.BACHELOR)
        .faculty(testFaculty).build();
    User testUser = user.toBuilder().build();
    User expectedUser = user.toBuilder().faculty(testFaculty).degree(testDegree).build();
    given(facultyService.getDegreeByTitleAndFieldOfStudy(any(Faculty.class), any(DegreeTitle.class),
        any(String.class))).willReturn(testDegree);
    given(facultyService.getByName(any(String.class))).willReturn(testFaculty);
    given(userRepository.findById(testUser.getId())).willReturn(Optional.of(testUser));

    userService.updateUser(PatchUserRequest.builder().facultyName(testFaculty.getName())
            .degreeField(testDegree.getFieldOfStudy()).degreeTitle(testDegree.getTitle()).build(),
        testUser.getId());

    verify(userRepository).save(expectedUser);
  }
  //endregion

  //region GetUserFaculty tests
  @Test
  public void getUserFacultyReturnsCorrectFaculty() {
    given(userRepository.findFacultyByUserId(any(Long.class))).willReturn(Optional.of(faculty));
    Faculty result = userService.getUserFaculty(user.getId());

    Assertions.assertEquals(faculty, result);
  }

  @Test
  public void getUserFacultyThrowsEntityNotFoundErrorWhenUserNotAssociatedWithFaculty() {
    given(userRepository.findFacultyByUserId(any(Long.class))).willReturn(Optional.empty());

    Assertions.assertThrows(EntityNotFoundException.class, () ->
        userService.getUserFaculty(user.getId()));
  }
  //endregion
}
