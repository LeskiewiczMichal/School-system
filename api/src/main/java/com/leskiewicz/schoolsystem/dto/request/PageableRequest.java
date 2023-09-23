package com.leskiewicz.schoolsystem.dto.request;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageableRequest {
  private int page = 0;
  private int size = 10;
  private String[] sort = {"id", "asc"};
  private String direction = "asc";

  public Pageable toPageable() {
    Sort.Direction sortDirection;
    String sortField;

    if (sort.length >= 2) {
      sortDirection = Sort.Direction.fromString(sort[1]);
      sortField = sort[0];
    } else {
      // If no direction is provided, use the default direction "asc"
      sortDirection = Sort.Direction.fromString(direction);
      sortField = sort[0];
    }

    return PageRequest.of(page, size, Sort.by(sortDirection, sortField));
  }


  public String toQueryParams() {
    String sortDirection;
    String sortField;

    if (sort.length >= 2) {
      sortDirection = sort[1];
      sortField = sort[0];
    } else {
      // If no direction is provided, use the default direction "asc"
      sortDirection = "asc";
      sortField = sort[0];
    }
    return "page="
        + getPage()
        + "&size="
        + getSize()
        + "&sort="
        + sortDirection + "," + sortField;

  }
}
