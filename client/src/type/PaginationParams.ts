enum SortDirection {
  ASC = "asc",
  DESC = "desc",
}

interface OptionalPaginationParams {
  page?: number;
  size?: number;
  sort?: [string, SortDirection];
}

interface PaginationParams {
  page: number;
  size: number;
  sort: [string, SortDirection];
}

export { SortDirection };
export type { OptionalPaginationParams, PaginationParams };
