package com.leskiewicz.schoolsystem.user;

import static com.leskiewicz.schoolsystem.builders.CourseBuilder.aCourse;
import static com.leskiewicz.schoolsystem.builders.CourseBuilder.courseDtoFrom;
import static com.leskiewicz.schoolsystem.builders.DegreeBuilder.aDegree;
import static com.leskiewicz.schoolsystem.builders.FacultyBuilder.aFaculty;
import static com.leskiewicz.schoolsystem.builders.TeacherDetailsBuilder.aTeacherDetails;
import static com.leskiewicz.schoolsystem.builders.UserBuilder.anUser;
import static com.leskiewicz.schoolsystem.builders.UserBuilder.userDtoFrom;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.error.customexception.UserAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.dto.PatchUserRequest;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.PatchTeacherDetailsRequest;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetailsRepository;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.utils.Mapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

  // Repositories
  @Mock private UserRepository userRepository;
  @Mock private CourseRepository courseRepository;
  @Mock private TeacherDetailsRepository teacherDetailsRepository;

  // Mappers
  @Mock private Mapper<User, UserDto> userMapper;
  @Mock private CourseMapper courseMapper;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private FileService fileService;


  @InjectMocks private UserServiceImpl userService;

  List<User> usersList =
      List.of(
          anUser().build(),
          anUser()
              .firstName("Johnny")
              .lastName("Silverhand")
              .email("johnny@example.com")
              .password("qwerty")
              .profilePictureName("Mypicture")
              .build());
  List<UserDto> userDtosList =
      List.of(userDtoFrom(usersList.get(0)), userDtoFrom(usersList.get(1)));
  User user = anUser().build();
  UserDto userDto = userDtoFrom(user);

  @Test
  public void getUsersReturnsPagedUserDtos() {
    when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(usersList));
    when(userMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(userDtosList));

    Page<UserDto> result = userService.getUsers(PageRequest.of(0, 1));

    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(userDtosList.get(0), result.getContent().get(0));
    Assertions.assertEquals(userDtosList.get(1), result.getContent().get(1));
  }

  @Nested
  public class getUserCourses {
    @Test
    public void returnsPagedCourseDtosWhenUserIsStudent() {
      List<Course> coursesList = List.of(aCourse().build());
      CourseDto courseDto = courseDtoFrom(aCourse().build());
      when(userRepository.findById(any(Long.class)))
          .thenReturn(Optional.ofNullable(anUser().build()));
      when(courseRepository.findCoursesByUserId(any(Long.class), any(Pageable.class)))
          .thenReturn(new PageImpl<>(coursesList));
      when(courseMapper.mapPageToDto(any(Page.class)))
          .thenReturn(new PageImpl<>(List.of(courseDto)));

      Page<CourseDto> result = userService.getUserCourses(1L, PageRequest.of(0, 1));

      assertCourseList(courseDto, result);
    }

    @Test
    public void throwsExceptionWhenUserIsNotTeacherOrStudent() {
      User user = anUser().role(Role.ROLE_ADMIN).build();
      given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));

      Assertions.assertThrows(
          EntityNotFoundException.class,
          () -> userService.getUserCourses(1L, PageRequest.of(0, 1)));
    }

    @Test
    public void returnsPagedCourseDtosWhenUserIsTeacher() {
      List<Course> coursesList = List.of(aCourse().build());
      CourseDto courseDto = courseDtoFrom(aCourse().build());
      when(userRepository.findById(any(Long.class)))
          .thenReturn(Optional.ofNullable(anUser().role(Role.ROLE_TEACHER).build()));
      when(courseMapper.mapPageToDto(any(Page.class)))
          .thenReturn(new PageImpl<>(List.of(courseDto)));
      when(courseRepository.findCoursesByTeacherId(any(Long.class), any(Pageable.class)))
          .thenReturn(new PageImpl<>(coursesList));

      Page<CourseDto> result = userService.getUserCourses(1L, PageRequest.of(0, 1));

      assertCourseList(courseDto, result);
    }

    @Test
    public void returns404IfUserDoesntExist() {
      given(userRepository.existsById(any(Long.class))).willReturn(false);

      Assertions.assertThrows(
          EntityNotFoundException.class, () -> userService.getUserCourses(1L, null));
    }

    private void assertCourseList(CourseDto courseDto, Page<CourseDto> result) {
      Assertions.assertEquals(1, result.getTotalElements());
      Assertions.assertEquals(courseDto, result.getContent().get(0));
    }
  }

  @Nested
  public class getUserFaculty {
    @Test
    public void returnsFacultyDto() {
      Faculty faculty = aFaculty().build();
      when(userRepository.findFacultyByUserId(any(Long.class))).thenReturn(Optional.of(faculty));

      Faculty result = userService.getUserFaculty(1L);

      Assertions.assertEquals(faculty, result);
    }

    @Test
    public void throwsExceptionWhenFacultyNotFound() {
      when(userRepository.findFacultyByUserId(any(Long.class))).thenReturn(Optional.empty());

      Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getUserFaculty(1L));
    }
  }

  @Nested
  public class getUserDegree {
    @Test
    public void getUserDegreeReturnsDegree() {
      Degree degree = aDegree().build();
      when(userRepository.findDegreeByUserId(any(Long.class))).thenReturn(Optional.of(degree));

      Degree result = userService.getUserDegree(1L);

      Assertions.assertEquals(degree, result);
    }

    @Test
    public void getUserDegreeThrowsExceptionWhenDegreeNotFound() {
      when(userRepository.findDegreeByUserId(any(Long.class))).thenReturn(Optional.empty());

      Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getUserDegree(1L));
    }
  }

  @Test
  public void searchReturnsPagedUserDtos() {
    String LAST_NAME = userDtosList.get(0).getLastName();
    String FIRST_NAME = userDtosList.get(0).getFirstName();
    Role ROLE = userDtosList.get(0).getRole();
    when(userRepository.searchUsersByLastNameAndFirstNameAndRole(
            LAST_NAME, FIRST_NAME, ROLE, PageRequest.of(0, 2)))
        .thenReturn(new PageImpl<>(usersList));
    when(userMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(userDtosList));

    Page<UserDto> result = userService.search(LAST_NAME, FIRST_NAME, ROLE, PageRequest.of(0, 2));

    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(2, result.getNumberOfElements());
    Assertions.assertEquals(0, result.getNumber());
    Assertions.assertEquals(2, result.getSize());
    Assertions.assertEquals(userDtosList.get(0), result.getContent().get(0));
    Assertions.assertEquals(userDtosList.get(1), result.getContent().get(1));
  }

  @Nested
  public class getTeacherDetails {
    @Test
    public void returnsCorrectTeacherDetails() {
      TeacherDetails teacherDetails = setUpGetTeacherDetailsTest();
      TeacherDetails testTeacherDetails = userService.getTeacherDetails(1L);

      Assertions.assertEquals(teacherDetails, testTeacherDetails);
      verify(userRepository).existsById(any(Long.class));
    }

    @Test
    public void callsExistsById() {
      setUpGetTeacherDetailsTest();
      userService.getTeacherDetails(1L);

      verify(userRepository).existsById(any(Long.class));
    }

    private TeacherDetails setUpGetTeacherDetailsTest() {
      TeacherDetails teacherDetails = aTeacherDetails().build();
      given(userRepository.existsById(any(Long.class))).willReturn(true);
      given(teacherDetailsRepository.findByUserId(any(Long.class)))
          .willReturn(Optional.of(teacherDetails));

      return teacherDetails;
    }

    @Test
    public void throwsEntityNotFound() {
      given(userRepository.existsById(any(Long.class))).willReturn(false);
      Assertions.assertThrows(
          EntityNotFoundException.class, () -> userService.getTeacherDetails(1L));
    }
  }

  @Nested
  public class getById {
    @Test
    public void returnsUserDto() {
      UserDto dto = userDtoFrom(user);
      when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
      when(userMapper.mapToDto(any(User.class))).thenReturn(dto);

      UserDto result = userService.getById(1L);

      Assertions.assertEquals(dto, result);
    }

    @Test
    public void throwsEntityNotFoundWhenUserWithGivenIdDoesntExist() {
      given(userRepository.findById(any(Long.class))).willReturn(Optional.empty());
      Assertions.assertThrows(EntityNotFoundException.class, () -> userService.getById(1L));
    }
  }

  @Nested
  public class getUserEmail {
    @Test
    public void returnsUser() {
      given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));
      User result = userService.getByEmail("email@example.com");
      Assertions.assertEquals(user, result);
    }

    @Test
    public void throwsEntityNotFound() {
      given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
      Assertions.assertThrows(
          EntityNotFoundException.class, () -> userService.getByEmail("email@example/com"));
    }
  }

  @Nested
  public class addUser {
    @Test
    public void savesUser() {
      given(userRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
      userService.addUser(user);
      verify(userRepository).save(user);
    }

    @Test
    public void throwsUserAlreadyExistsExceptionWhenUserWithGivenEmailAlreadyExists() {
      given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));
      Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(user));
    }
  }

  @Nested
  public class updateUser {
    @Test
    public void updatesAndSavesUser() {
      User user = anUser().build();
      User userSpy = spy(user);
      PatchUserRequest request =
          PatchUserRequest.builder()
              .firstName("Test")
              .lastName("Testing")
              .email("testing@example.com")
              .password("TestPassword")
              .build();

      given(userRepository.findById(user.getId())).willReturn(Optional.of(userSpy));
      Mockito.lenient().when(passwordEncoder.encode(any(String.class))).thenReturn("encoded");
      Mockito.lenient()
          .when(userRepository.findByEmail(any(String.class)))
          .thenReturn(Optional.empty());

      userService.updateUser(request, user.getId());

      verify(userRepository).save(userSpy);
      verify(userSpy).update(any(PatchUserRequest.class), any(PasswordEncoder.class));
    }

    @Test
    public void throwsUserAlreadyExistsExceptionWhenUserWithGivenEmailAlreadyExists() {
      PatchUserRequest request =
          PatchUserRequest.builder()
              .firstName("Test")
              .lastName("Testing")
              .email("test@example.com")
              .password("password")
              .build();

      given(userRepository.findById(any(Long.class))).willReturn(Optional.of(user));
      given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

      Assertions.assertThrows(
          UserAlreadyExistsException.class, () -> userService.updateUser(request, 1L));
    }
  }

  @Test
  public void addProfilePictureToUser() {
    MultipartFile file = Mockito.mock(MultipartFile.class);

    given(fileService.uploadImage(any(MultipartFile.class))).willReturn("imagePath");
    given(userRepository.findById(any(Long.class)))
            .willReturn(Optional.of(TestHelper.createUser(Mockito.mock(Faculty.class), Mockito.mock(Degree.class))));

    userService.addImage(1L, file);

    verify(userRepository, Mockito.times(1)).save(any(User.class));
    verify(fileService, Mockito.times(1)).uploadImage(any(MultipartFile.class));
  }

  @Nested
  public class updateTeacherDetails {
    @Test
    public void savesProperTeacherDetails() {
      PatchTeacherDetailsRequest changeRequest =
          PatchTeacherDetailsRequest.builder()
              .bio("New bio")
              .title(DegreeTitle.DOCTOR)
              .tutorship("New tutorship")
              .degreeField("New degree field")
              .build();
      TeacherDetails teacherDetails = aTeacherDetails().build();
      TeacherDetails spyTeacherDetails = spy(teacherDetails);
      given(teacherDetailsRepository.findByUserId(any(Long.class)))
          .willReturn(Optional.of(spyTeacherDetails));

      TeacherDetails teacherDetailsChanged = userService.updateTeacherDetails(changeRequest, 1L);

      verify(teacherDetailsRepository).save(teacherDetailsChanged);
      verify(spyTeacherDetails).update(changeRequest);
    }

    @Test
    public void throwsEntityNotFoundExceptionWhenTeacherDetailsNotFound() {
      given(teacherDetailsRepository.findByUserId(any(Long.class))).willReturn(Optional.empty());
      Assertions.assertThrows(
          EntityNotFoundException.class, () -> userService.updateTeacherDetails(null, 1L));
    }
  }
}
