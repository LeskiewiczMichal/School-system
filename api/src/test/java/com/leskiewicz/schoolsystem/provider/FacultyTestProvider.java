package com.leskiewicz.schoolsystem.provider;

import com.leskiewicz.schoolsystem.testModels.FacultyDto;
import com.leskiewicz.schoolsystem.testUtils.assertions.FacultyDtoAssertions;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.stream.Stream;

public class FacultyTestProvider {

    public static Stream<Arguments> getFacultiesCollectionTest(String basePath) {
        // Variables needed for tests
        FacultyDtoAssertions assertions = new FacultyDtoAssertions();
        FacultyDto informatics = FacultyDto.builder().id(101L).name("Informatics").build();
        FacultyDto biology = FacultyDto.builder().id(102L).name("Biology").build();
        FacultyDto electronics = FacultyDto.builder().id(103L).name("Electronics").build();
        FacultyDto chemistry = FacultyDto.builder().id(104L).name("Chemistry").build();
        FacultyDto sociology = FacultyDto.builder().id(111L).name("Sociology").build();
        FacultyDto law = FacultyDto.builder().id(112L).name("Law").build();
        FacultyDto economics = FacultyDto.builder().id(113L).name("Economics").build();

        // Arguments
        Arguments noParams = Arguments.of(basePath, Arrays.asList(informatics, biology, electronics), assertions);
        Arguments pageOne = Arguments.of(basePath + "?page=1", Arrays.asList(sociology, law, economics), assertions);
        Arguments descending = Arguments.of(basePath + "?direction=desc", Arrays.asList(economics, law, sociology), assertions);
        Arguments sortByName = Arguments.of(basePath + "?sort=name", Arrays.asList(biology, chemistry), assertions);
        Arguments pageSize20 = Arguments.of(basePath + "?size=20", Arrays.asList(informatics, biology, electronics, chemistry), assertions);

        return Stream.of(noParams, pageOne, descending, sortByName, pageSize20);
    }
}
