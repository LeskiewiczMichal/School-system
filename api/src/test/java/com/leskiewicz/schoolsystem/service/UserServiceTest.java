package com.leskiewicz.schoolsystem.service;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeService;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.security.Role;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.UserService;
import com.leskiewicz.schoolsystem.user.UserServiceImpl;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
}
