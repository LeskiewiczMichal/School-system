package com.leskiewicz.schoolsystem.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {

  @Value("${upload.path.images}")
  private String uploadImagesPath;

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return modelMapper;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/images/**")
            .addResourceLocations(uploadImagesPath);
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
