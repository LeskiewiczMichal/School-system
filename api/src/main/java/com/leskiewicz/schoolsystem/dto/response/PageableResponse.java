package com.leskiewicz.schoolsystem.dto.response;

import com.leskiewicz.schoolsystem.controller.UserController;
import com.leskiewicz.schoolsystem.dto.request.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@Builder
@AllArgsConstructor
public class PageableResponse<T> {

    private PagedModel<T> pagedModel;
    private int currentPage;
    private int totalPages;
    private Long totalElements;

    public void addLinks(Page<?> pages, PageableRequest request) {
        // Self link
        UriComponentsBuilder selfLinkBuilder = linkTo(methodOn(UserController.class).getUsers(0, 0)).toUriComponentsBuilder();
        String query = UriComponentsBuilder.fromUriString("")
                .queryParams(request.toQueryParams())
                .build()
                .toUriString();
        selfLinkBuilder.replaceQuery(query);
        Link selfLink = Link.of(selfLinkBuilder.build().toUriString(), "self");
        pagedModel.add(selfLink);

        // Next page link
        if (pages.hasNext()) {
            PageableRequest nextRequest = new PageableRequest();
            nextRequest.setPage(pages.nextPageable().getPageNumber());
            nextRequest.setSize(request.getSize());
            nextRequest.setSort(request.getSort());
            nextRequest.setDirection(request.getDirection());

//            UriComponentsBuilder nextLinkBuilder = linkTo(methodOn(UserController.class).getUsers(nextRequest)).toUriComponentsBuilder();
            String nextQuery = UriComponentsBuilder.fromUriString("")
                    .queryParams(nextRequest.toQueryParams())
                    .build()
                    .toUriString();
            selfLinkBuilder.replaceQuery(nextQuery);
            Link nextLink = Link.of(selfLinkBuilder.build().toUriString(), "next");

            pagedModel.add(nextLink);
        }
        // Previous page link
        if (pages.hasPrevious()) {
            PageableRequest prevRequest = new PageableRequest();
            prevRequest.setPage(pages.previousPageable().getPageNumber());
            prevRequest.setSize(request.getSize());
            prevRequest.setSort(request.getSort());
            prevRequest.setDirection(request.getDirection());

            String prevQuery = UriComponentsBuilder.fromUriString("")
                    .queryParams(prevRequest.toQueryParams())
                    .build()
                    .toUriString();
            selfLinkBuilder.replaceQuery(prevQuery);
            Link prevLink = Link.of(selfLinkBuilder.build().toUriString(), "prev");

            pagedModel.add(prevLink);
        }
    }

}
