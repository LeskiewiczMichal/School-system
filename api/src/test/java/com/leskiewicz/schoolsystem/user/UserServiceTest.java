package com.leskiewicz.schoolsystem.user;

import static com.leskiewicz.schoolsystem.builders.UserBuilder.anUser;
import static com.leskiewicz.schoolsystem.builders.UserBuilder.userDtoFrom;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.leskiewicz.schoolsystem.builders.UserBuilder;
import com.leskiewicz.schoolsystem.course.Course;
import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetailsRepository;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

  // Repositories
  @Mock private UserRepository userRepository;
  @Mock private CourseRepository courseRepository;
  @Mock private TeacherDetailsRepository teacherDetailsRepository;

  // Mappers
  @Mock private UserMapper userMapper;
  @Mock private CourseMapper courseMapper;

  @InjectMocks private UserServiceImpl userService;

  Faculty faculty;
  Degree degree;

  @BeforeEach
  public void setUp() {
    faculty = Mockito.mock(Faculty.class);
    degree = Mockito.mock(Degree.class);
  }

  @Test
  public void getUsersReturnsPagedUsers() {
    List<User> usersList = List.of(anUser().build());
    List<UserDto> userDtosList = List.of(userDtoFrom(anUser().build()));
    when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(usersList));
    when(userMapper.convertToDto(any(User.class))).thenReturn(userDtosList.get(0));

    Page<UserDto> result = userService.getUsers(PageRequest.of(0, 1));

    Assertions.assertEquals(1, result.getTotalElements());
    Assertions.assertEquals(userDtosList.get(0), result.getContent().get(0));
  }

//  @Test
//  public void getUserCoursesReturnsPagedCourses() {
//    List<Course> coursesList = List.of(TestHelper.createCourse(faculty));
//    List<CourseDto> courseDtosList = List.of(TestHelper.createCourseDto(faculty));
//    when(courseRepository.findCoursesByUserId(any(User.class), any(Pageable.class)))
//        .thenReturn(new PageImpl<>(coursesList));
//    when(courseMapper.convertToDto(any(Course.class))).thenReturn(courseDtosList.get(0));
//
//    Page<CourseDto> result = userService.getUserCourses(TestHelper.createUser(faculty, degree), PageRequest.of(0, 1));
//
//    Assertions.assertEquals(1, result.getTotalElements());
//    Assertions.assertEquals(courseDtosList.get(0), result.getContent().get(0));
//  }
}
