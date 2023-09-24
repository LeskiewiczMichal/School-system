package com.leskiewicz.schoolsystem.faculty;

import com.leskiewicz.schoolsystem.course.CourseRepository;
import com.leskiewicz.schoolsystem.course.utils.CourseMapper;
import com.leskiewicz.schoolsystem.degree.DegreeRepository;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapper;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import com.leskiewicz.schoolsystem.testUtils.TestHelper;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.utils.UserMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static com.leskiewicz.schoolsystem.builders.FacultyBuilder.aFaculty;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {
    List<Faculty> faculties = List.of(aFaculty().build(), aFaculty().name("Faculty of Electronics").id(2L).build());
    List<FacultyDto> facultyDtos = List.of()

    @Mock private FacultyRepository facultyRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private DegreeRepository degreeRepository;
    @Mock private UserRepository userRepository;

    // Mappers
    @Mock private FacultyMapper facultyMapper;
    @Mock private CourseMapper courseMapper;
    @Mock private DegreeMapper degreeMapper;
    @Mock private UserMapper userMapper;

    @InjectMocks
    private FacultyServiceImpl facultyService;
}
