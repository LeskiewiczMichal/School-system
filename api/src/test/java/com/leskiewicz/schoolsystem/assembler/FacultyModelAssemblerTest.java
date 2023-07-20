package com.leskiewicz.schoolsystem.assembler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
import com.leskiewicz.schoolsystem.faculty.dto.FacultyDto;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyMapper;
import com.leskiewicz.schoolsystem.faculty.utils.FacultyModelAssembler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@ExtendWith(MockitoExtension.class)
public class FacultyModelAssemblerTest {

  @Mock
  private FacultyMapper facultyMapper;

  @InjectMocks
  private FacultyModelAssembler facultyModelAssembler;

  // Variables
  Faculty faculty;
  FacultyDto facultyDto;

  @BeforeEach
  public void setup() {
    faculty = Mockito.mock(Faculty.class);
    facultyDto = FacultyDto.builder().id(0L).name("Computer Science").build();

    given(facultyMapper.convertToDto(any(Faculty.class))).willReturn(facultyDto);
  }

  @Test
  public void testToModel() {
    Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(FacultyController.class).getFacultyById(0L))
        .withSelfRel();

    FacultyDto result = facultyModelAssembler.toModel(faculty);

    Assertions.assertEquals(facultyDto, result);
    Assertions.assertEquals(selfLink, result.getLink("self").get());
  }
}
