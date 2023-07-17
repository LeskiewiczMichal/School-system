package com.leskiewicz.schoolsystem.service.links;

import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

public interface PageableLinksService {

    public <T, D> void addLinks(CollectionModel<D> resources, Page<T> page, Class<?> controllerClass, PageableRequest request);
    public <T, D> void addLinks(CollectionModel<D> resources, Page<T> page, Class<?> controllerClass, PageableRequest request, String requestRelativePath);
}
