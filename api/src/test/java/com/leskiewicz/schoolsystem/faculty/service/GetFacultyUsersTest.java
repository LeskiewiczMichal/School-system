package com.leskiewicz.schoolsystem.faculty.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.leskiewicz.schoolsystem.authentication.Role;
import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.faculty.FacultyServiceImpl;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
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
public class GetFacultyUsersTest {

  // Variables
  Faculty faculty;
  // Mocks
  @Mock private FacultyRepository facultyRepository;
  @Mock private UserMapper userMapper;
  @InjectMocks private FacultyServiceImpl facultyService;

  @BeforeEach
  public void setup() {

    // Set up test data
    faculty = Faculty.builder().id(1L).name("Software Engineering").build();
  }

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
                Role.ROLE_STUDENT),
            new User(
                2L,
                "Jane",
                "Smith",
                "jane.smith@example.com",
                "12345",
                faculty,
                degree,
                Role.ROLE_STUDENT));
    Page<User> usersPage = new PageImpl<>(userList);

    given(facultyRepository.findFacultyUsers(any(Long.class), any(Pageable.class), any(Role.class)))
        .willReturn(usersPage);

    // Mock the behavior of the userMapper
    UserDto userDto1 =
        new UserDto(1L, "John", "Doe", "john.doe@example.com", "Some Faculty", "Some Degree", 1L);
    UserDto userDto2 =
        new UserDto(
            2L, "Jane", "Smith", "jane.smith@example.com", "Another Faculty", "Another Degree", 2L);
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
    verify(facultyRepository, times(1))
        .findFacultyUsers(any(Long.class), any(Pageable.class), any(Role.class));
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
