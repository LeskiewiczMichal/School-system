package com.leskiewicz.schoolsystem.controller;

import com.leskiewicz.schoolsystem.faculty.FacultyRepository;
import com.leskiewicz.schoolsystem.testModels.CustomLink;
import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import com.leskiewicz.schoolsystem.testModels.UserDto;
import com.leskiewicz.schoolsystem.testUtils.assertions.FacultyDtoAssertions;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;


@Sql(scripts = {"classpath:schema.sql", "classpath:facultyController.sql"})
public class FacultyControllerTest extends GenericControllerTest<UserDto> {

  private final String GET_FACULTIES = "/api/faculties";
  private final String GET_FACULTY_BY_ID = "/api/faculties/";

  @Autowired
  private FacultyRepository facultyRepository;


  //region GetUsers tests
  static Stream<Arguments> getApiCollectionResponsesProvider() {
    FacultyDto facultyDto = FacultyDto.builder().id(1L).name("Informatics").build();

    Arguments noParams = Arguments.of("/api/faculties", Arrays.asList(facultyDto), Arrays.asList(
            CustomLink.builder().rel("self")
                .href("http://localhost/api/faculties?page=0&size=10&sort=id&direction=asc").build()),
        new FacultyDtoAssertions()

    );

    return Stream.of(noParams);
  }
  //endregion
}
