package com.leskiewicz.schoolsystem.degree;

import com.leskiewicz.schoolsystem.course.dto.CourseDto;
import com.leskiewicz.schoolsystem.course.utils.CourseDtoAssembler;
import com.leskiewicz.schoolsystem.degree.dto.DegreeDto;
import com.leskiewicz.schoolsystem.degree.utils.DegreeDtoAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PagedResourcesAssembler;

@ExtendWith(MockitoExtension.class)
public class DegreeControllerTest {

  private DegreeService degreeService;

  // Assemblers
  private DegreeDtoAssembler degreeDtoAssembler;
  private CourseDtoAssembler courseDtoAssembler;
  private PagedResourcesAssembler<DegreeDto> degreePagedResourcesAssembler;
  private PagedResourcesAssembler<CourseDto> coursePagedResourcesAssembler;

  DegreeController degreeController;

  // Annotation mocks didn't work as expected here, pagedResourcesAssemblers were mixed up
  @BeforeEach
  public void setUp() {
    // Create mock instances
    degreeService = Mockito.mock(DegreeService.class);
    degreeDtoAssembler = Mockito.mock(DegreeDtoAssembler.class);
    courseDtoAssembler = Mockito.mock(CourseDtoAssembler.class);
    degreePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);
    coursePagedResourcesAssembler = Mockito.mock(PagedResourcesAssembler.class);

    // Create DegreeController instance
    degreeController =
        new DegreeController(
            degreeService,
            degreeDtoAssembler,
            courseDtoAssembler,
            degreePagedResourcesAssembler,
            coursePagedResourcesAssembler);
  }
}
