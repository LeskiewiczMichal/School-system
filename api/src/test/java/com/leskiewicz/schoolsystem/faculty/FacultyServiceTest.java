package com.leskiewicz.schoolsystem.faculty;

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
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import com.leskiewicz.schoolsystem.generic.CommonTests;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import com.leskiewicz.schoolsystem.utils.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
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

import static com.leskiewicz.schoolsystem.builders.CourseBuilder.aCourse;
import static com.leskiewicz.schoolsystem.builders.CourseBuilder.courseDtoFrom;
import static com.leskiewicz.schoolsystem.builders.DegreeBuilder.aDegree;
import static com.leskiewicz.schoolsystem.builders.DegreeBuilder.degreeDtoFrom;
import static com.leskiewicz.schoolsystem.builders.FacultyBuilder.aFaculty;
import static com.leskiewicz.schoolsystem.builders.FacultyBuilder.facultyDtoFrom;
import static com.leskiewicz.schoolsystem.builders.UserBuilder.anUser;
import static com.leskiewicz.schoolsystem.builders.UserBuilder.userDtoFrom;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {
  List<Faculty> facultiesList =
      List.of(aFaculty().build(), aFaculty().name("Faculty of Electronics").id(2L).build());
  List<FacultyDto> facultyDtosList =
      List.of(facultyDtoFrom(facultiesList.get(0)), facultyDtoFrom(facultiesList.get(1)));
  Faculty faculty = aFaculty().build();
  FacultyDto facultyDto = facultyDtoFrom(faculty);

  @Mock private FacultyRepository facultyRepository;
  @Mock private CourseRepository courseRepository;
  @Mock private DegreeRepository degreeRepository;
  @Mock private UserRepository userRepository;
  @Mock private Mapper<Faculty, FacultyDto> facultyMapper;
  @Mock private CourseMapper courseMapper;
  @Mock private DegreeMapper degreeMapper;
  @Mock private Mapper<User, UserDto> userMapper;

  @InjectMocks private FacultyServiceImpl facultyService;

  @Test
  public void getFacultiesReturnsPagedFacultyDtos() {
    when(facultyRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(facultiesList));
    when(facultyMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(facultyDtosList));

    Page<FacultyDto> result = facultyService.getFaculties(PageRequest.of(0, 2));

    Assertions.assertEquals(2, result.getTotalElements());
    Assertions.assertEquals(facultyDtosList.get(0), result.getContent().get(0));
    Assertions.assertEquals(facultyDtosList.get(1), result.getContent().get(1));
  }

  @Nested
  public class getById {
    @Test
    public void returnsFacultyDto() {
      when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));
      when(facultyMapper.mapToDto(any(Faculty.class))).thenReturn(facultyDto);

      FacultyDto result = facultyService.getById(1L);

      Assertions.assertEquals(facultyDto, result);
    }

    @Test
    public void throwsExceptionWhenUserWithGivenIdDoesntExist() {
      when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.empty());
      Assertions.assertThrows(EntityNotFoundException.class, () -> facultyService.getById(1L));
    }
  }

  @Nested
  public class getByName {
    @Test
    public void getByNameReturnsFacultyDto() {
      when(facultyRepository.findByName(faculty.getName())).thenReturn(Optional.of(faculty));

      Faculty testFaculty = facultyService.getByName(faculty.getName());

      Assertions.assertEquals(faculty, testFaculty);
    }

    @Test
    public void getByNameThrowsExceptionWhenFacultyDoesntExist() {
      when(facultyRepository.findByName(faculty.getName())).thenReturn(Optional.empty());
      Assertions.assertThrows(
          EntityNotFoundException.class, () -> facultyService.getByName(faculty.getName()));
    }
  }

  @Nested
  public class getByTitleAndFieldOfStudy {
    @Test
    public void getDegreeByTitleAndFieldOfStudyReturnsCorrectDegree() {
      Degree degree = aDegree().build();
      Faculty faculty = aFaculty().degrees(List.of(degree)).build();

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
              facultyService.getDegreeByTitleAndFieldOfStudy(
                  faculty, DegreeTitle.BACHELOR, "test"));
    }
  }

  @Nested
  public class getFacultyCourses {
    List<Course> coursesList = List.of(aCourse().build(), aCourse().title("Testing").build());
    List<CourseDto> courseDtosList = List.of(courseDtoFrom(coursesList.get(0)), courseDtoFrom(coursesList.get(1)));

    @Test
    public void returnsPagedCourses() {
      when(facultyRepository.existsById(any(Long.class))).thenReturn(true);
      when(courseRepository.findCoursesByFacultyId(any(Long.class), any(Pageable.class))).thenReturn(new PageImpl<>(coursesList));
      when(courseMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(courseDtosList));

      Page<CourseDto> result = facultyService.getFacultyCourses(1L, PageRequest.of(0, 2));

      Assertions.assertEquals(2, result.getTotalElements());
      Assertions.assertEquals(courseDtosList.get(0), result.getContent().get(0));
      Assertions.assertEquals(courseDtosList.get(1), result.getContent().get(1));
    }

    @Test
    public void throwsExceptionWhenFacultyDoesntExist() {
      Pageable pageable = Mockito.mock(PageRequest.class);

      when(facultyRepository.existsById(any(Long.class))).thenReturn(false);

      Assertions.assertThrows(
              EntityNotFoundException.class, () -> facultyService.getFacultyCourses(1L, pageable));
    }
  }

  @Nested
  public class getFacultyDegrees {
    List<Degree> degreesList = List.of(aDegree().build(), aDegree().build());
    List<DegreeDto> degreeDtosList = List.of(degreeDtoFrom(degreesList.get(0)), degreeDtoFrom(degreesList.get(1)));
    @Test
    public void returnsPagedDegrees() {
      when(facultyRepository.existsById(any(Long.class))).thenReturn(true);
      when(degreeRepository.findDegreesByFacultyId(any(Long.class), any(Pageable.class))).thenReturn(new PageImpl<>(degreesList));
      when(degreeMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(degreeDtosList));

      Page<DegreeDto> result = facultyService.getFacultyDegrees(1L, PageRequest.of(0, 2));

      Assertions.assertEquals(2, result.getTotalElements());
      Assertions.assertEquals(degreeDtosList.get(0), result.getContent().get(0));
      Assertions.assertEquals(degreeDtosList.get(1), result.getContent().get(1));
    }

    @Test
    public void throwsExceptionWhenFacultyDoesntExist() {
      Pageable pageable = Mockito.mock(PageRequest.class);

      when(facultyRepository.existsById(any(Long.class))).thenReturn(false);

      Assertions.assertThrows(
              EntityNotFoundException.class,
              () -> facultyService.getFacultyDegrees(faculty.getId(), pageable));
    }
  }

  @Nested
  public class getFacultyUsers {
    List<User> usersList = List.of(anUser().build(), anUser().firstName("Testing").lastName("FacultyTest").email("testingemail@example.com").build());
    List<UserDto> userDtosList = List.of(userDtoFrom(usersList.get(0)), userDtoFrom(usersList.get(1)));

    @Test
    public void returnsPagedUsers() {
      when(userRepository.findUsersByFacultyId(
              any(Long.class), any(Pageable.class), any(Role.class)))
          .thenReturn(new PageImpl<>(usersList));
      when(userMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(userDtosList));
      when(facultyRepository.existsById(any(Long.class))).thenReturn(true);

      Page<UserDto> result =
          facultyService.getFacultyUsers(1L, PageRequest.of(0, 2), Role.ROLE_STUDENT);

//      Assertions.assertEquals(2, result.getTotalElements());
      Assertions.assertEquals(userDtosList.get(0), result.getContent().get(0));
      Assertions.assertEquals(userDtosList.get(1), result.getContent().get(1));
    }

    @Test
    public void throwsExceptionWhenFacultyDoesntExist() {
      Pageable pageable = Mockito.mock(PageRequest.class);

      when(facultyRepository.existsById(any(Long.class))).thenReturn(false);

      Assertions.assertThrows(
              EntityNotFoundException.class,
              () -> facultyService.getFacultyUsers(faculty.getId(), pageable, Role.ROLE_STUDENT));
    }
  }
}
