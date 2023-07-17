package com.leskiewicz.schoolsystem.service.links;

import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class PageableLinksServiceImpl implements PageableLinksService {

    @Override
    public <T, D> void addLinks(CollectionModel<D> resources, Page<T> page, Class<?> controllerClass, PageableRequest request) {

        // Builder to make links from the base uri
        UriComponentsBuilder linkBuilder = linkTo(controllerClass).toUriComponentsBuilder();
        addLinksHelper(resources, page, request, linkBuilder);
    }

    @Override
    public <T, D> void addLinks(CollectionModel<D> resources, Page<T> page, Class<?> controllerClass, PageableRequest request, String requestRelativePath) {

        // Builder to make links from the base uri
        UriComponentsBuilder linkBuilder = linkTo(controllerClass).slash(requestRelativePath).toUriComponentsBuilder();
        addLinksHelper(resources, page, request, linkBuilder);
    }

    private <T, D> void addLinksHelper(CollectionModel<D> resources, Page<T> page, PageableRequest request, UriComponentsBuilder linkBuilder) {

        // Request to modify the query parameters
        PageableRequest req = PageableRequest.builder()
                .page(request.getPage())
                .size(request.getSize())
                .sort(request.getSort())
                .direction(request.getDirection())
                .build();

        // Self link
        linkBuilder.replaceQuery(request.toQueryParams());
        Link selfLink = Link.of(linkBuilder.build().toUriString(), "self");
        resources.add(selfLink);

        // First link
        req.setPage(0);
        linkBuilder.replaceQuery(req.toQueryParams());
        Link firstLink = Link.of(linkBuilder.build().toUriString(), "first");
        resources.add(firstLink);

        // Last link
        int lastPage = page.getTotalPages() - 1;
        req.setPage(lastPage);
        linkBuilder.replaceQuery(req.toQueryParams());
        Link lastLink = Link.of(linkBuilder.build().toUriString(), "last");
        resources.add(lastLink);

        if (page.hasNext()) {
            // Next link
            req.setPage(page.nextPageable().getPageNumber());
            linkBuilder.replaceQuery(req.toQueryParams());
            Link nextLink = Link.of(linkBuilder.build().toUriString(), "next");
            resources.add(nextLink);
        }
        if (page.hasPrevious()) {
            // Previous link
            req.setPage(page.previousPageable().getPageNumber());
            linkBuilder.replaceQuery(req.toQueryParams());
            Link prevLink = Link.of(linkBuilder.build().toUriString(), "prev");
            resources.add(prevLink);

        }
    }
}
