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
import com.leskiewicz.schoolsystem.error.customexception.EntityAlreadyExistsException;
import com.leskiewicz.schoolsystem.faculty.dto.CreateFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.dto.PatchFacultyRequest;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import com.leskiewicz.schoolsystem.user.User;
import com.leskiewicz.schoolsystem.user.UserRepository;
import com.leskiewicz.schoolsystem.user.dto.UserDto;
import com.leskiewicz.schoolsystem.utils.Mapper;
import com.leskiewicz.schoolsystem.utils.Support;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {

  @Mock private Support support;
  @Mock private FacultyRepository facultyRepository;
  @Mock private CourseRepository courseRepository;
  @Mock private DegreeRepository degreeRepository;
  @Mock private UserRepository userRepository;
  @Mock private FacultyMapper facultyMapper;
  @Mock private Mapper<User, UserDto> userMapper;
  @Mock private DegreeMapper degreeMapper;
  @Mock private CourseMapper courseMapper;
  @InjectMocks private FacultyServiceImpl facultyService;

  List<Faculty> facultiesList = createFacultiesList();
  List<FacultyDto> facultyDtosList = createFacultyDtosList();
  Faculty faculty = aFaculty().build();
  FacultyDto facultyDto = facultyDtoFrom(faculty);

  @Test
  public void getFacultiesReturnsPagedFacultyDtos() {
    when(facultyRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(facultiesList));
    when(facultyMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(facultyDtosList));

    Page<FacultyDto> facultyDtosPage = facultyService.getFaculties(Pageable.unpaged());

    Assertions.assertEquals(2, facultyDtosPage.getTotalElements());
    Assertions.assertEquals(
        facultyDtosList.get(0).getName(), facultyDtosPage.getContent().get(0).getName());
    Assertions.assertEquals(
        facultyDtosList.get(1).getName(), facultyDtosPage.getContent().get(1).getName());
  }

  @Nested
  public class getById {
    @Test
    public void returnsFacultyDto() {
      when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
      when(facultyMapper.mapToDto(any(Faculty.class))).thenReturn(facultyDto);

      FacultyDto facultyDtoFromService = facultyService.getById(1L);

      Assertions.assertEquals(facultyDto.getName(), facultyDtoFromService.getName());
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
    public void returnsFacultyDto() {
      when(facultyRepository.findByName(any(String.class))).thenReturn(Optional.of(faculty));

      Faculty result = facultyService.getByName("Test");

      Assertions.assertEquals(faculty.getName(), result.getName());
    }

    @Test
    public void throwsExceptionWhenFacultyDoesntExist() {
      when(facultyRepository.findByName(any(String.class))).thenReturn(Optional.empty());

      Assertions.assertThrows(
          EntityNotFoundException.class, () -> facultyService.getByName("Test"));
    }
  }

  @Nested
  public class getByTitleAndFieldOfStudy {
    @Test
    public void returnsCorrectDegree() {
      Degree degree = aDegree().build();
      Faculty faculty = aFaculty().degrees(List.of(degree)).build();

      Degree result =
          facultyService.getDegreeByTitleAndFieldOfStudy(
              faculty, degree.getTitle(), degree.getFieldOfStudy());

      Assertions.assertEquals(degree.getTitle(), result.getTitle());
    }

    @Test
    public void throwsEntityNotFound() {
      Assertions.assertThrows(
          EntityNotFoundException.class,
          () ->
              facultyService.getDegreeByTitleAndFieldOfStudy(
                  faculty, DegreeTitle.MASTER, "Test"));
    }
  }

  @Nested
  public class getFacultyCourses {
    List<Course> coursesList = List.of(aCourse().build(), aCourse().title("Testing").build());
    List<CourseDto> courseDtosList =
            List.of(courseDtoFrom(coursesList.get(0)), courseDtoFrom(coursesList.get(1)));

    @Test
    public void returnsPagedCourses() {
      when(facultyRepository.existsById(any(Long.class))).thenReturn(true);
      when(courseRepository.findCoursesByFacultyId(any(Long.class), any(Pageable.class)))
          .thenReturn(new PageImpl<>(coursesList));
      when(courseMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(courseDtosList));

      Page<CourseDto> courseDtosPage = facultyService.getFacultyCourses(1L, PageRequest.of(0, 2));

      Assertions.assertEquals(2, courseDtosPage.getTotalElements());
      Assertions.assertEquals(
          courseDtosList.get(0).getTitle(), courseDtosPage.getContent().get(0).getTitle());
      Assertions.assertEquals(
          courseDtosList.get(1).getTitle(), courseDtosPage.getContent().get(1).getTitle());
    }

    @Test
    public void throwsExceptionWhenFacultyDoesntExist() {
      when(facultyRepository.existsById(any(Long.class))).thenReturn(false);

      Assertions.assertThrows(
          EntityNotFoundException.class,
          () -> facultyService.getFacultyCourses(1L, Pageable.unpaged()));
    }
  }

  @Nested
  public class getFacultyDegrees {
    List<Degree> degreesList =
            List.of(aDegree().build(), aDegree().fieldOfStudy("Testing").build());
    List<DegreeDto> degreeDtosList =
            List.of(degreeDtoFrom(degreesList.get(0)), degreeDtoFrom(degreesList.get(1)));

    @Test
    public void returnsPagedDegrees() {
      when(facultyRepository.existsById(any(Long.class))).thenReturn(true);
      when(degreeRepository.findDegreesByFacultyId(any(Long.class), any(Pageable.class)))
          .thenReturn(new PageImpl<>(degreesList));
      when(degreeMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(degreeDtosList));

      Page<DegreeDto> result = facultyService.getFacultyDegrees(1L, Pageable.unpaged());

      Assertions.assertEquals(2, result.getTotalElements());
      Assertions.assertEquals(
          degreeDtosList.get(0).getTitle(), result.getContent().get(0).getTitle());
      Assertions.assertEquals(
          degreeDtosList.get(1).getTitle(), result.getContent().get(1).getTitle());
    }

    @Test
    public void throwsExceptionWhenFacultyDoesntExist() {
      when(facultyRepository.existsById(any(Long.class))).thenReturn(false);

      Assertions.assertThrows(
          EntityNotFoundException.class,
          () -> facultyService.getFacultyDegrees(1L, Pageable.unpaged()));
    }
  }

  @Nested
  public class getFacultyUsers {
    List<User> usersList =
            List.of(
                    anUser().build(),
                    anUser().firstName("Testing").lastName("Tester").email("testemail@test.pl").build());
    List<UserDto> usersDtosList =
            List.of(userDtoFrom(usersList.get(0)), userDtoFrom(usersList.get(1)));

    @Test
    public void returnsPagedUsers() {
      when(facultyRepository.existsById(any(Long.class))).thenReturn(true);
      when(userRepository.findUsersByFacultyId(
              any(Long.class), any(Pageable.class), any(Role.class)))
          .thenReturn(new PageImpl<>(usersList));
      when(userMapper.mapPageToDto(any(Page.class))).thenReturn(new PageImpl<>(usersDtosList));

      Page<UserDto> result =
          facultyService.getFacultyUsers(1L, Pageable.unpaged(), Role.ROLE_STUDENT);

      Assertions.assertEquals(2, result.getTotalElements());
      Assertions.assertEquals(
          usersDtosList.get(0).getFirstName(), result.getContent().get(0).getFirstName());
      Assertions.assertEquals(
          usersDtosList.get(1).getFirstName(), result.getContent().get(1).getFirstName());
    }

    @Test
    public void throwsExceptionWhenFacultyDoesntExist() {
      when(facultyRepository.existsById(any(Long.class))).thenReturn(false);

      Assertions.assertThrows(
          EntityNotFoundException.class,
          () -> facultyService.getFacultyUsers(1L, Pageable.unpaged(), Role.ROLE_STUDENT));
    }
  }

  @Nested
  public class createFaculty {
    @Test
    public void savesAndReturnsFacultyDto() {
      CreateFacultyRequest createFacultyRequest = new CreateFacultyRequest("Faculty of Tests");
      FacultyDto facultyDto = facultyDtoFrom(aFaculty().name("Faculty of Tests").build());
      when(facultyRepository.save(any(Faculty.class)))
          .thenReturn(aFaculty().name("Faculty of Tests").build());
      when(facultyMapper.mapToDto(any(Faculty.class))).thenReturn(facultyDto);

      FacultyDto facultyDtoFromService = facultyService.createFaculty(createFacultyRequest);

      Assertions.assertEquals(facultyDto.getName(), facultyDtoFromService.getName());
      verify(support).notifyCreated(any(String.class), any(Long.class));
    }

    @Test
    public void throwsConstraintViolationExceptionOnRequestInvalid() {
      CreateFacultyRequest request = new CreateFacultyRequest(null);

      Assertions.assertThrows(
          ConstraintViolationException.class, () -> facultyService.createFaculty(request));
    }

    @Test
    public void throwsEntityAlreadyExistsExceptionOnNameThatIsAlreadyTaken() {
      CreateFacultyRequest request = new CreateFacultyRequest("Test");
      given(facultyRepository.findByName(any(String.class)))
          .willReturn(Optional.of(aFaculty().build()));

      Assertions.assertThrows(
          EntityAlreadyExistsException.class, () -> facultyService.createFaculty(request));
    }
  }

  @Nested
  public class updateFaculty {
    @Test
    public void updateFacultySavesCorrectFaculty() {
      Faculty faculty = aFaculty().build();
      given(facultyRepository.findById(any(Long.class))).willReturn(Optional.of(faculty));
      PatchFacultyRequest request = new PatchFacultyRequest("new name");

      FacultyDto facultyDto = facultyDtoFrom(faculty);
      given(facultyMapper.mapToDto(any(Faculty.class))).willReturn(facultyDto);

      FacultyDto result = facultyService.updateFaculty(request, faculty.getId());

      faculty.setName("New name");

      verify(facultyRepository).save(faculty);
      Assertions.assertEquals(facultyDto, result);
    }

    @Test
    public void throwsEntityNotFound() {
      when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

      Assertions.assertThrows(
          EntityNotFoundException.class,
          () -> facultyService.updateFaculty(new PatchFacultyRequest("Test"), 1L));
    }

    @Test
    public void throwsExceptionWhenFacultyWithTheSameNameExists() {
      given(facultyRepository.findById(any(Long.class)))
          .willReturn(Optional.of(aFaculty().build()));
      given(facultyRepository.findByName(any(String.class)))
          .willReturn(Optional.of(aFaculty().build()));

      Assertions.assertThrows(
          EntityAlreadyExistsException.class,
          () -> facultyService.updateFaculty(new PatchFacultyRequest("Test"), 1L));
    }
  }

  private List<Faculty> createFacultiesList() {
    return List.of(aFaculty().build(), aFaculty().name("Faculty of Electronics").build());
  }

  private List<FacultyDto> createFacultyDtosList() {
    return List.of(
        facultyDtoFrom(aFaculty().build()),
        facultyDtoFrom(aFaculty().name("Faculty of Electronics").build()));
  }
}
