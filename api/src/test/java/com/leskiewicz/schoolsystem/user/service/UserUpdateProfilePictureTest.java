package com.leskiewicz.schoolsystem.user.service;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.files.FileService;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserUpdateProfilePictureTest {

    @Mock
    private FileService fileService;

    @Mock private UserRepository userRepository;

    @InjectMocks private UserServiceImpl userService;

    @Test
    public void addProfilePictureToUser() {
        // Prepare data
        MultipartFile file = Mockito.mock(MultipartFile.class);

        // Mocks
        given(fileService.uploadImage(any(MultipartFile.class))).willReturn("imagePath");
        given(userRepository.findById(any(Long.class)))
                .willReturn(Optional.of(TestHelper.createUser(Mockito.mock(Faculty.class), Mockito.mock(Degree.class))));

        // Call the method
        userService.addImage(1L, file);

        // Verify
        verify(userRepository, Mockito.times(1)).save(any(User.class));
        verify(fileService, Mockito.times(1)).uploadImage(any(MultipartFile.class));
    }
}
