package com.leskiewicz.schoolsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

  public String toQueryParams() {
    return "page="
        + getPage()
        + "&size="
        + getSize()
        + "&sort="
        + getSort()
        + "&direction="
        + getDirection();
  }
}
