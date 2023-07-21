package com.leskiewicz.schoolsystem.testUtils.assertions;

import org.springframework.test.web.servlet.ResultActions;

public interface DtoAssertion<T> {

  void assertDtoInCollection(ResultActions result, int index, T dto) throws Exception;

  void assertDto(ResultActions result, T dto) throws Exception;
}
