package com.leskiewicz.schoolsystem.config;

import com.leskiewicz.schoolsystem.controller.AuthenticationController;
import com.leskiewicz.schoolsystem.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class SpringDataRestConfiguration implements RepositoryRestConfigurer {

    @Bean
    public RepresentationModelProcessor<RepositoryLinksResource> repositoryLinksResourceProcessor() {
        return new RepresentationModelProcessor<RepositoryLinksResource>() {
            @Override
            public RepositoryLinksResource process(RepositoryLinksResource resource) {
                Link registerLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).register(null)).withRel("register");
                Link authenticateLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).authenticate(null)).withRel("authenticate");

                resource.add(registerLink, authenticateLink);
                return resource;
            }
        };
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.getExposureConfiguration()
                .forDomainType(User.class)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(HttpMethod.POST));
    }
}
