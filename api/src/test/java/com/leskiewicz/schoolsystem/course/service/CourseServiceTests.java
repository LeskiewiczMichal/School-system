package com.leskiewicz.schoolsystem.course.service;

import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.CourseServiceImpl;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTests {

    @Mock
    private CourseRepository courseRepository;
    @Mock private FacultyRepository facultyRepository;
    @Mock private UserRepository userRepository;
    @Mock private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    public void isUserEnrolledTest() {
        given(courseRepository.existsCourseStudentRelation(1L, 1L)).willReturn(true);

        boolean res = courseService.isUserEnrolled(1L, 1L);
        Assertions.assertEquals(true, res);
    }
}
