package com.leskiewicz.schoolsystem.user.service;

import com.leskiewicz.schoolsystem.degree.DegreeService;
import com.leskiewicz.schoolsystem.degree.DegreeTitle;
import com.leskiewicz.schoolsystem.faculty.FacultyService;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.UserServiceImpl;
import com.leskiewicz.schoolsystem.user.teacherdetails.PatchTeacherDetailsRequest;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetails;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetailsModelAssembler;
import com.leskiewicz.schoolsystem.user.teacherdetails.TeacherDetailsRepository;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UpdateTeacherDetailsTest {

  // Mocks
  @Mock private UserRepository userRepository;
  @Mock private DegreeService degreeService;
  @Mock private FacultyService facultyService;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserMapper userMapper;
  @Mock private TeacherDetailsRepository teacherDetailsRepository;
  @InjectMocks private UserServiceImpl userService;

  @Test
  public void updateTeacherDetailsWithProperDataSavesProperTeacherDetails() {
    // Prepare data
    User user = Mockito.mock(User.class);
    TeacherDetails teacherDetails = TestHelper.createTeacherDetails(user);
    PatchTeacherDetailsRequest request =
        PatchTeacherDetailsRequest.builder()
            .bio("New bio")
            .title(DegreeTitle.DOCTOR)
            .tutorship("New tutorship")
            .degreeField("New degree field")
            .build();

    // Mocks
    given(teacherDetailsRepository.findByUserId(any(Long.class)))
        .willReturn(Optional.of(teacherDetails));

    // Call the method
    TeacherDetails teacherDetailsChanged = userService.updateTeacherDetails(request, user.getId());

    // Verify
    verify(teacherDetailsRepository).save(teacherDetailsChanged);
    // Assert
    Assertions.assertEquals(request.getBio(), teacherDetailsChanged.getBio());
    Assertions.assertEquals(request.getTutorship(), teacherDetailsChanged.getTutorship());
    Assertions.assertEquals(request.getDegreeField(), teacherDetailsChanged.getDegreeField());
    Assertions.assertEquals(request.getTitle(), teacherDetailsChanged.getTitle());
  }
}
