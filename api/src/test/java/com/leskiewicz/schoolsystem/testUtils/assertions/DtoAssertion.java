package com.leskiewicz.schoolsystem.testUtils.assertions;

import com.leskiewicz.schoolsystem.testModels.DegreeDto;
import org.springframework.test.web.servlet.ResultActions;

public interface DtoAssertion<T> {

  void assertDtoInCollection(ResultActions result, int index, T dto) throws Exception;

  void assertDto(ResultActions result, T dto) throws Exception;
  void assertDtoWithAnyId(ResultActions result, T dto) throws Exception;
}
