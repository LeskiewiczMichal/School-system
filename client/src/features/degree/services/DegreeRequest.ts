import {
  OptionalPaginationParams,
  PaginationParams,
  SortDirection,
} from "../../../type/PaginationParams";
import APILink from "../../../type/APILink";
import DegreeTitle from "../types/DegreeTitle";
import PaginationInfo from "../../../type/PaginationInfo";
import Degree from "../types/Degree";
import RequestService from "../../../utils/RequestService";

export type FetchDegreesProps = {
  link: APILink;
  faculty?: string;
  title?: DegreeTitle;
  pagination?: OptionalPaginationParams;
};

export interface GetDegreesResponse {
  degrees: Degree[];
  paginationInfo: PaginationInfo;
}

const getDegrees = async (
  props: FetchDegreesProps,
): Promise<GetDegreesResponse> => {
  // Prepare the link
  const { link, faculty, title, pagination } = props;

  // Prepare the search parameters
  let params = {};
  if (faculty) {
    params = {
      ...params,
      faculty: faculty,
    };
  }
  if (title) {
    params = {
      ...params,
      category: title,
    };
  }

  // Prepare the pagination
  let paginationParams: PaginationParams = {
    page: 0,
    size: 10,
    sort: ["id", SortDirection.DESC],
  };
  if (pagination) {
    paginationParams = {
      page: pagination.page ? pagination.page : paginationParams.page,
      size: pagination.size ? pagination.size : paginationParams.size,
      sort: pagination.sort ? pagination.sort : paginationParams.sort,
    };
  }

  const responseData = await RequestService.performGetRequest({
    link: link,
    pagination: paginationParams,
    params: params,
  });
};

const DegreeRequest = {
  getDegrees,
};

export default DegreeRequest;
