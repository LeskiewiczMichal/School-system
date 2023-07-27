package com.leskiewicz.schoolsystem.course.service;

import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.CourseServiceImpl;
import com.leskiewicz.schoolsystem.course.dto.CreateCourseRequest;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateCourseTest {

    @Mock private CourseRepository courseRepository;
    @Mock private FacultyRepository facultyRepository;
    @Mock private UserRepository userRepository;
    @Mock private CourseMapper courseMapper;

    @InjectMocks private CourseServiceImpl courseService;

    @Test
    public void createsAndReturnCourseOnProperRequest() {
        // Mock data
        Faculty faculty = Mockito.mock(Faculty.class);
        User teacher = Mockito.mock(User.class);
    // Create a mock request
    CreateCourseRequest request = CreateCourseRequest.builder()
            .title("Course Title")
            .durationInHours(10)
            .facultyId(1L)
            .teacherId(1L)
            .build();
    }
}
