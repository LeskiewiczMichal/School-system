package com.leskiewicz.schoolsystem.faculty.service;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceGettersTest {

  // Repositories
  @Mock private FacultyRepository facultyRepository;
  @Mock private CourseRepository courseRepository;
  @Mock private DegreeRepository degreeRepository;
  @Mock private UserRepository userRepository;

  // Mappers
  @Mock private FacultyMapper facultyMapper;
  @Mock private CourseMapper courseMapper;
  @Mock private DegreeMapper degreeMapper;
  @Mock private UserMapper userMapper;

  @InjectMocks private FacultyServiceImpl facultyService;

  Faculty faculty;

  @BeforeEach
  public void setup() {
    faculty = TestHelper.createFaculty();
  }

  // *** GetByName ***//
  @Test
  public void getFacultiesReturnsPagedFaculties() {
    List<Faculty> faculties = Arrays.asList(TestHelper.createFaculty(), TestHelper.createFaculty());
    List<FacultyDto> facultyDtos =
        Arrays.asList(
            TestHelper.createFacultyDto("TestFaculty"), TestHelper.createFacultyDto("TestFaculty"));

    CommonTests.serviceGetAll(
        Faculty.class,
        faculties,
        facultyDtos,
        facultyRepository::findAll,
        facultyMapper::convertToDto,
        facultyService::getFaculties);
  }

  @Test
  public void getByIdReturnsCorrectFaculty() {
    Faculty faculty = TestHelper.createFaculty();
    FacultyDto facultyDto = TestHelper.createFacultyDto("FacultyName");

    CommonTests.serviceGetById(
        Faculty.class,
        faculty,
        facultyDto,
        facultyRepository::findById,
        facultyMapper::convertToDto,
        facultyService::getById);
  }

  @Test
  public void getByIdThrowsEntityNotFound() {
    given(facultyRepository.findById(faculty.getId())).willReturn(Optional.empty());

    Assertions.assertThrows(
        EntityNotFoundException.class, () -> facultyService.getById(faculty.getId()));
  }

  // *** GetByName *** //

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

  // *** GetByDegreeTitleAndFieldOfStudy ***//
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

  // *** GetFacultyCourses ***//
  @Test
  public void getFacultyCoursesReturnsPagedCourses() {
    Faculty faculty = Mockito.mock(Faculty.class);
    User teacher = Mockito.mock(User.class);
    List<Course> courses =
        Arrays.asList(
            TestHelper.createCourse(faculty, teacher), TestHelper.createCourse(faculty, teacher));
    List<CourseDto> courseDtos =
        Arrays.asList(
            TestHelper.createCourseDto("Test", "Teacher name"),
            TestHelper.createCourseDto("Testing", "Name Teacher"));

    CommonTests.serviceGetAllResourcesRelated(
        Course.class,
        courses,
        courseDtos,
        courseRepository::findCoursesByFacultyId,
        courseMapper::convertToDto,
        facultyRepository::existsById,
        facultyService::getFacultyCourses);
  }

  @Test
  public void getFacultyCoursesReturns404IfFacultyDoesntExist() {
    Pageable pageable = Mockito.mock(PageRequest.class);

    given(facultyRepository.existsById(any(Long.class))).willReturn(false);

    Assertions.assertThrows(
        EntityNotFoundException.class, () -> facultyService.getFacultyCourses(1L, pageable));
  }

  // *** GetFacultyDegrees ***//

  @Test
  public void getFacultyDegreesReturnsPagedDegrees() {
    List<Degree> degrees =
        Arrays.asList(TestHelper.createDegree(faculty), TestHelper.createDegree(faculty));
    List<DegreeDto> degreeDtos =
        Arrays.asList(
            TestHelper.createDegreeDto(faculty.getName()),
            TestHelper.createDegreeDto("TestFaculty"));

    CommonTests.serviceGetAllResourcesRelated(
        Degree.class,
        degrees,
        degreeDtos,
        degreeRepository::findDegreesByFacultyId,
        degreeMapper::convertToDto,
        facultyRepository::existsById,
        facultyService::getFacultyDegrees);
  }

  @Test
  public void getFacultyDegreesReturns404IfFacultyDoesntExist() {
    Pageable pageable = Mockito.mock(PageRequest.class);

    given(facultyRepository.existsById(any(Long.class))).willReturn(false);

    Assertions.assertThrows(
        EntityNotFoundException.class,
        () -> facultyService.getFacultyDegrees(faculty.getId(), pageable));
  }

  // *** GetFacultyUsers ***//
  @Test
  public void getFacultyUsersReturnsPagedUsers() {
    Faculty faculty = Mockito.mock(Faculty.class);
    Degree degree = Mockito.mock(Degree.class);
    List<User> userList =
        Arrays.asList(
            new User(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                "12345",
                faculty,
                degree,
                null,
                Role.ROLE_STUDENT),
            new User(
                2L,
                "Jane",
                "Smith",
                "jane.smith@example.com",
                "12345",
                faculty,
                degree,
                null,
                Role.ROLE_STUDENT));
    Page<User> usersPage = new PageImpl<>(userList);

    given(
            userRepository.findUsersByFacultyId(
                any(Long.class), any(Pageable.class), any(Role.class)))
        .willReturn(usersPage);

    // Mock the behavior of the userMapper
    UserDto userDto1 =
        new UserDto(
            1L,
            "John",
            "Doe",
            "john.doe@example.com",
            "Some Faculty",
            Role.ROLE_STUDENT.toString(),
            1L,
            "Some Degree",
            2L);
    UserDto userDto2 =
        new UserDto(
            2L,
            "Jane",
            "Smith",
            "jane.smith@example.com",
            "Another Faculty",
            Role.ROLE_STUDENT.toString(),
            2L,
            "Another Degree",
            2L);
    given(userMapper.convertToDto(any(User.class))).willReturn(userDto1, userDto2);
    given(facultyRepository.existsById(any(Long.class))).willReturn(true);
    // Call the method to test
    Page<UserDto> result =
        facultyService.getFacultyUsers(1L, PageRequest.of(0, 10), Role.ROLE_STUDENT);

    // Assert the result
    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(userDto1, result.getContent().get(0));
    Assertions.assertEquals(userDto2, result.getContent().get(1));

    // Verify the interactions with userRepository and userMapper
    verify(userRepository, times(1))
        .findUsersByFacultyId(any(Long.class), any(Pageable.class), any(Role.class));
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
}
