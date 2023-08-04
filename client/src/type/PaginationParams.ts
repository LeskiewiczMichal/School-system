enum SortDirection {
  ASC = "asc",
  DESC = "desc",
}

interface PaginationParams {
  page: number;
  size: number;
  sort: [string, SortDirection];
}

export { SortDirection };
export default PaginationParams;
