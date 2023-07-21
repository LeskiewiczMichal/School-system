package com.leskiewicz.schoolsystem.assembler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyModelAssembler;
import io.jsonwebtoken.lang.Assert;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@ExtendWith(MockitoExtension.class)
public class FacultyModelAssemblerTest {

  // Variables
  Faculty faculty;
  FacultyDto facultyDto;
  @Mock private FacultyMapper facultyMapper;
  @InjectMocks private FacultyModelAssembler facultyModelAssembler;

  @BeforeEach
  public void setup() {
    faculty = Mockito.mock(Faculty.class);
    facultyDto = FacultyDto.builder().id(0L).name("Computer Science").build();

    given(facultyMapper.convertToDto(any(Faculty.class))).willReturn(facultyDto);
  }

  @Test
  public void testToModel() {
    Link selfLink =
        WebMvcLinkBuilder.linkTo(methodOn(FacultyController.class).getFacultyById(0L))
            .withSelfRel();
    Link studentsLink =
        WebMvcLinkBuilder.linkTo(methodOn(FacultyController.class).getFacultyStudents(0L, null))
            .withRel("students");
    Link teachersLink =
        WebMvcLinkBuilder.linkTo(methodOn(FacultyController.class).getFacultyTeachers(0L, null))
            .withRel("teachers");

    FacultyDto result = facultyModelAssembler.toModel(faculty);

    Assertions.assertEquals(facultyDto, result);
    Assertions.assertEquals(selfLink, result.getLink("self").get());
    Assertions.assertEquals(studentsLink, result.getLink("students").get());
    Assertions.assertEquals(teachersLink, result.getLink("teachers").get());
  }

  @Test
  public void testToCollectionModel() {
    // Create collection with a single faculty
    Iterable<Faculty> faculties = Collections.singleton(faculty);

    CollectionModel<FacultyDto> facultyDtos = facultyModelAssembler.toCollectionModel(faculties);

    Assert.notEmpty(facultyDtos.getContent());
    facultyDtos
        .getContent()
        .forEach(
            dto -> {
              // Assert every faculty has correct links
              Link selfLink = dto.getLink("self").get();
              Link studentsLink = dto.getLink("students").get();
              Link teachersLink = dto.getLink("teachers").get();

              Assertions.assertNotNull(selfLink, "DegreeDto should have a self link");
              Assertions.assertNotNull(studentsLink, "DegreeDto should have a students link");
              Assertions.assertNotNull(teachersLink, "DegreeDto should have a teachers link");
            });
  }
}
