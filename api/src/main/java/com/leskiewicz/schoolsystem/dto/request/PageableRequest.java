package com.leskiewicz.schoolsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageableRequest {
    private int page = 0;
    private int size = 10;
    private String sort = "id";
    private String direction = "asc";

    public Pageable toPageable() {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
    }

//    public MultiValueMap<String, String> toQueryParams() {
//        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
//        queryParams.add("page", String.valueOf(getPage()));
//        queryParams.add("size", String.valueOf(getSize()));
//        queryParams.add("sort", getSort());
//        queryParams.add("direction", getDirection());
//        return queryParams;
//    }

    public String toQueryParams() {
        return "page=" + getPage() +
                "&size=" + getSize() +
                "&sort=" + getSort() +
                "&direction=" + getDirection();
    }
}
