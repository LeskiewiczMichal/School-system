package com.leskiewicz.schoolsystem.assembler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.leskiewicz.schoolsystem.degree.Degree;
import com.leskiewicz.schoolsystem.degree.DegreeController;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeMapperImpl;
import com.leskiewicz.schoolsystem.degree.utils.DegreeModelAssembler;
import com.leskiewicz.schoolsystem.faculty.Faculty;
import com.leskiewicz.schoolsystem.faculty.FacultyController;
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
public class DegreeModelAssemblerTest {

  @Mock
  private DegreeMapperImpl degreeMapperMock;

  @InjectMocks
  private DegreeModelAssembler degreeModelAssembler;

  // Variables
  Degree degree;
  Faculty faculty;
  DegreeDto degreeDto;

  @BeforeEach
  public void setup() {
    faculty = Mockito.mock(Faculty.class);
    degree = Mockito.mock(Degree.class);

    given(degree)
  }

  @Test
  public void testToModel() {
    given(faculty.getName()).willReturn("FacultyName");
    given(faculty.getId()).willReturn(0L);

    Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(DegreeController.class).getDegreeById(0L))
        .withSelfRel();
    Link facultyLink = WebMvcLinkBuilder.linkTo(
        methodOn(FacultyController.class).getFacultyById(0L)).withRel("faculty");

    DegreeDto result = degreeModelAssembler.toModel(degree);

    Assertions.assertEquals(result.getLink("self").get(), selfLink);
    Assertions.assertEquals(result.getLink("faculty").get(), facultyLink);
  }

  @Test
  public void testToCollectionModel() {
    // Create collection with a single faculty
    Iterable<Degree> degrees = Collections.singleton(degree);

    CollectionModel<DegreeDto> degreeDtos = degreeModelAssembler.toCollectionModel(degrees);

    Assert.notEmpty(degreeDtos.getContent());
    degreeDtos.getContent().forEach(dto -> {
      // Assert every degree has correct links
      Link selfLink = dto.getLink("self").get();
      Link facultyLink = dto.getLink("faculty").get();

      Assertions.assertNotNull(selfLink, "DegreeDto should have a self link");
      Assertions.assertNotNull(facultyLink, "DegreeDto should have a self link");
    });
  }

}
