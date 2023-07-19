package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeService;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.error.UserAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.UserServiceImpl;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    //region Mocks
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private DegreeService degreeService;
    @Mock
    private FacultyService facultyService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;
    //endregion

    // Variables
    Faculty faculty;
    Degree degree;
    User user;

    @BeforeEach
    public void setUp() {
        // Set up test data
        faculty = new Faculty();
        faculty.setName("Engineering");

        degree = Degree.builder()
                .title(DegreeTitle.BACHELOR)
                .fieldOfStudy("Computer Science")
                .faculty(faculty)
                .build();

        user = User.builder()
                .email("test@example.com")
                .firstName("Tester")
                .lastName("Testing")
                .password("encoded_password")
                .role(Role.ROLE_STUDENT)
                .faculty(faculty)
                .degree(degree)
                .build();
    }

    //region GetById tests
    @Test
    public void getByIdHappyPath() {
        given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));

        User testUser = userService.getById(1L);

        Assertions.assertEquals(user, testUser);
    }

    @Test
    public void getByIdThrowsEntityNotFound() {
        given(userRepository.findById(any(Long.class))).willReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                userService.getById(1L));
    }
    //endregion

    //region GetByEmail tests
    @Test
    public void getByEmailHappyPath() {
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

        User testUser = userService.getByEmail("email@example.com");

        Assertions.assertEquals(user, testUser);
    }

    @Test
    public void getByEmailThrowsEntityNotFound() {
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                userService.getByEmail("email@example/com"));
    }
    //endregion

    //region GetUsers tests
    @Test
    public void getUsersReturnsPagedUsers() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("asc"), "id"));
        Page<User> mockPage = Mockito.mock(Page.class);

        given(userRepository.findAll(pageable)).willReturn(mockPage);

        Page<User> users = userService.getUsers(pageable);
        Assertions.assertEquals(users, mockPage);
    }
    //endregion

    //region ToUserDtos tests
    @Test
    public void toUserDtosReturnsListOfUserDtos() {
        Page<User> userPage = new PageImpl<>(Arrays.asList(user, user));
        UserDto userDto = Mockito.mock(UserDto.class);
        given(userMapper.convertToDto(any(User.class))).willReturn(userDto);

        List<UserDto> result = userService.toUserDtos(userPage);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(userDto, result.get(0));
        Assertions.assertEquals(userDto, result.get(1));
    }
    //endregion

    //region AddUser tests
    @Test
    public void addUserSavesUser() {
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

        userService.addUser(user);

        verify(userRepository).save(user);
    }

    @Test
    public void throwsUserAlreadyExistsExceptionWhenUserWithGivenEmailAlreadyExists() {
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

        Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                userService.addUser(user));
    }
    //endregion

    //region UpdateUser Tests
    @ParameterizedTest
    @MethodSource("updateUserWithDifferentFieldsSavesProperUserProvider")
    public void updateUserWithDifferentFieldsSavesProperUser(PatchUserRequest request, User baseUser, User expectedUserToSave) {
        given(userRepository.findById(baseUser.getId())).willReturn(Optional.of(baseUser));
        Mockito.lenient().when(passwordEncoder.encode(any(String.class))).thenReturn("encoded");
        Mockito.lenient().when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        userService.updateUser(request, baseUser.getId());

        verify(userRepository).save(expectedUserToSave);
    }

    @Test
    public void updateUserDegree() {
        Degree testDegree = Degree.builder()
                .fieldOfStudy("Testing")
                .title(DegreeTitle.BACHELOR)
                .faculty(faculty)
                .build();
        User testUser = user.toBuilder().build();
        User expectedUser = user.toBuilder()
                .degree(testDegree)
                .build();
        given(facultyService.getDegreeByTitleAndFieldOfStudy(any(Faculty.class), any(DegreeTitle.class), any(String.class))).willReturn(testDegree);
        given(userRepository.findById(testUser.getId())).willReturn(Optional.of(testUser));

        userService.updateUser(PatchUserRequest.builder().degreeTitle(testDegree.getTitle()).degreeField(testDegree.getFieldOfStudy()).build(), testUser.getId());

        verify(userRepository).save(expectedUser);
    }


    @Test
    public void updateUserFaculty() {
        Faculty testFaculty = Faculty.builder()
                .name("test")
                .build();
        User testUser = user.toBuilder().build();
        User expectedUser = user.toBuilder()
                .faculty(testFaculty)
                .build();
        given(facultyService.getDegreeByTitleAndFieldOfStudy(any(Faculty.class), any(DegreeTitle.class), any(String.class))).willReturn(degree);
        given(facultyService.getByName(any(String.class))).willReturn(testFaculty);
        given(userRepository.findById(testUser.getId())).willReturn(Optional.of(testUser));

        userService.updateUser(PatchUserRequest.builder().facultyName(testFaculty.getName()).build(), testUser.getId());

        verify(userRepository).save(expectedUser);
    }

    @Test
    public void updateUserFacultyAndDegree() {
        Faculty testFaculty = Faculty.builder()
                .name("test")
                .build();
        Degree testDegree = Degree.builder()
                .fieldOfStudy("Testing")
                .title(DegreeTitle.BACHELOR)
                .faculty(testFaculty)
                .build();
        User testUser = user.toBuilder().build();
        User expectedUser = user.toBuilder()
                .faculty(testFaculty)
                .degree(testDegree)
                .build();
        given(facultyService.getDegreeByTitleAndFieldOfStudy(any(Faculty.class), any(DegreeTitle.class), any(String.class))).willReturn(testDegree);
        given(facultyService.getByName(any(String.class))).willReturn(testFaculty);
        given(userRepository.findById(testUser.getId())).willReturn(Optional.of(testUser));

        userService.updateUser(PatchUserRequest.builder().facultyName(testFaculty.getName()).degreeField(testDegree.getFieldOfStudy()).degreeTitle(testDegree.getTitle()).build(), testUser.getId());

        verify(userRepository).save(expectedUser);
    }

    static Stream<Arguments> updateUserWithDifferentFieldsSavesProperUserProvider() {
        Faculty baseFaculty = Faculty.builder()
                .name("Engineering")
                .build();

        Degree baseDegree = Degree.builder()
                .title(DegreeTitle.BACHELOR)
                .fieldOfStudy("Computer Science")
                .faculty(baseFaculty)
                .build();

        User baseUser = User.builder()
                .email("test@example.com")
                .firstName("Tester")
                .lastName("Testing")
                .password("encoded_password")
                .role(Role.ROLE_STUDENT)
                .faculty(baseFaculty)
                .degree(baseDegree)
                .build();


        return Stream.of(
                Arguments.of(PatchUserRequest.builder().firstName("Test").build(), baseUser.toBuilder().build(), baseUser.toBuilder().firstName("Test").build()),
                Arguments.of(PatchUserRequest.builder().lastName("Test").build(), baseUser.toBuilder().build(), baseUser.toBuilder().lastName("Test").build()),
                Arguments.of(PatchUserRequest.builder().email("test@example.com").build(),  baseUser.toBuilder().build(), baseUser.toBuilder().email("test@example.com").build()),
                Arguments.of(PatchUserRequest.builder().password("Test").build(),  baseUser.toBuilder().build(), baseUser.toBuilder().password("encoded").build())
        );
    }
    //endregion
}
