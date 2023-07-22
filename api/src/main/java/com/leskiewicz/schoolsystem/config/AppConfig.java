package com.leskiewicz.schoolsystem.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return modelMapper;
  }

  @Bean
  public HateoasSortHandlerMethodArgumentResolver sortResolver() {
    return new HateoasSortHandlerMethodArgumentResolver();
  }


  @Bean
  public HateoasPageableHandlerMethodArgumentResolver pageableResolver() {
    return new HateoasPageableHandlerMethodArgumentResolver(sortResolver());
  }

  @Bean
  public PagedResourcesAssembler<?> pagedResourcesAssembler() {
    return new PagedResourcesAssembler<>(pageableResolver(), null);
  }
}
