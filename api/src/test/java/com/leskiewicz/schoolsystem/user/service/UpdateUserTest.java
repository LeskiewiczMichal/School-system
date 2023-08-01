package com.leskiewicz.schoolsystem.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeService;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.UserServiceImpl;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import com.leskiewicz.schoolsystem.user.utils.UserMapperImpl;
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
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UpdateUserTest {

  // Variables
  Faculty faculty;
  Degree degree;
  User user;

  // Mocks
  @Mock private UserRepository userRepository;
  @Mock private FacultyService facultyService;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserMapper userMapper = new UserMapperImpl();
  @InjectMocks private UserServiceImpl userService;

  static Stream<Arguments> updateUserWithDifferentFieldsSavesProperUserProvider() {
    Faculty baseFaculty = Faculty.builder().name("Engineering").build();

    Degree baseDegree =
        Degree.builder()
            .title(DegreeTitle.BACHELOR)
            .fieldOfStudy("Computer Science")
            .faculty(baseFaculty)
            .build();

    User baseUser =
        User.builder()
            .email("test@example.com")
            .firstName("Tester")
            .lastName("Testing")
            .password("encoded_password")
            .role(Role.ROLE_STUDENT)
            .faculty(baseFaculty)
            .degree(baseDegree)
            .build();

    return Stream.of(
        Arguments.of(
            PatchUserRequest.builder().firstName("Test").build(),
            baseUser.toBuilder().build(),
            baseUser.toBuilder().firstName("Test").build()),
        Arguments.of(
            PatchUserRequest.builder().lastName("Test").build(),
            baseUser.toBuilder().build(),
            baseUser.toBuilder().lastName("Test").build()),
        Arguments.of(
            PatchUserRequest.builder().email("test@example.com").build(),
            baseUser.toBuilder().build(),
            baseUser.toBuilder().email("test@example.com").build()),
        Arguments.of(
            PatchUserRequest.builder().password("Test").build(),
            baseUser.toBuilder().build(),
            baseUser.toBuilder().password("encoded").build()));
  }

  @BeforeEach
  public void setUp() {
    faculty = Mockito.mock(Faculty.class);
    degree = Mockito.mock(Degree.class);
    user = TestHelper.createUser(faculty, degree);
  }

  @ParameterizedTest
  @MethodSource("updateUserWithDifferentFieldsSavesProperUserProvider")
  public void updateUserWithDifferentFieldsSavesProperUser(
      PatchUserRequest request, User baseUser, User expectedUserToSave) {
    given(userRepository.findById(baseUser.getId())).willReturn(Optional.of(baseUser));
    Mockito.lenient().when(passwordEncoder.encode(any(String.class))).thenReturn("encoded");
    Mockito.lenient()
        .when(userRepository.findByEmail(any(String.class)))
        .thenReturn(Optional.empty());

    userService.updateUser(request, baseUser.getId());

    verify(userRepository).save(expectedUserToSave);
  }

  @Test
  public void updateUserDegree() {
    Degree testDegree =
        Degree.builder()
            .fieldOfStudy("Testing")
            .title(DegreeTitle.BACHELOR)
            .faculty(faculty)
            .build();
    User testUser = user.toBuilder().build();
    User expectedUser = user.toBuilder().degree(testDegree).build();

    given(
            facultyService.getDegreeByTitleAndFieldOfStudy(
                any(Faculty.class), any(DegreeTitle.class), any(String.class)))
        .willReturn(testDegree);
    given(userRepository.findById(testUser.getId())).willReturn(Optional.of(testUser));

    userService.updateUser(
        PatchUserRequest.builder()
            .degreeTitle(testDegree.getTitle())
            .degreeField(testDegree.getFieldOfStudy())
            .build(),
        testUser.getId());

    verify(userRepository).save(expectedUser);
  }

  @Test
  public void updateUserFaculty() {
    Faculty testFaculty = Faculty.builder().name("test").degrees(Arrays.asList(degree)).build();
    User testUser = user.toBuilder().build();
    User expectedUser = user.toBuilder().faculty(testFaculty).build();

    given(degree.getFieldOfStudy()).willReturn("mock");
    given(degree.getTitle()).willReturn(DegreeTitle.BACHELOR);
    given(
            facultyService.getDegreeByTitleAndFieldOfStudy(
                any(Faculty.class), any(DegreeTitle.class), any(String.class)))
        .willReturn(degree);
    given(facultyService.getByName(any(String.class))).willReturn(testFaculty);
    given(userRepository.findById(testUser.getId())).willReturn(Optional.of(testUser));

    userService.updateUser(
        PatchUserRequest.builder().facultyName("test").build(), testUser.getId());

    verify(userRepository).save(expectedUser);
  }

  @Test
  public void updateUserFacultyAndDegree() {
    Faculty testFaculty = Faculty.builder().name("test").build();
    Degree testDegree =
        Degree.builder()
            .fieldOfStudy("Testing")
            .title(DegreeTitle.BACHELOR)
            .faculty(testFaculty)
            .build();
    User testUser = user.toBuilder().build();
    User expectedUser = user.toBuilder().faculty(testFaculty).degree(testDegree).build();
    given(
            facultyService.getDegreeByTitleAndFieldOfStudy(
                any(Faculty.class), any(DegreeTitle.class), any(String.class)))
        .willReturn(testDegree);
    given(facultyService.getByName(any(String.class))).willReturn(testFaculty);
    given(userRepository.findById(testUser.getId())).willReturn(Optional.of(testUser));

    userService.updateUser(
        PatchUserRequest.builder()
            .facultyName(testFaculty.getName())
            .degreeField(testDegree.getFieldOfStudy())
            .degreeTitle(testDegree.getTitle())
            .build(),
        testUser.getId());

    verify(userRepository).save(expectedUser);
  }
}
