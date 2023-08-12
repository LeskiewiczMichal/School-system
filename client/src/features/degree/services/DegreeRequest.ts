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
import DegreeMapper from "../mapper/DegreeMapper";
import mapPaginationInfoFromServer from "../../../utils/MapPaginationInfoFromServer";

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

/**
 * Fetch degrees from the API (using search or not)
 *
 * @param props {@link FetchDegreesProps} object,
 * containing the link {@link APILink} - either a search link or a link to all articles,
 * faculty id (optional),
 * title {@link DegreeTitle} (optional) and pagination {@link OptionalPaginationParams} (optional)
 * @returns Promise of an array of {@link Degree} objects
 */
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

  // Convert the response data into degrees
  const degreesArr: Degree[] = DegreeMapper.mapArrayFromServerData(
    responseData._embedded.degrees,
  );
  const paginationInfo: PaginationInfo =
    mapPaginationInfoFromServer(responseData);

  return {
    degrees: degreesArr,
    paginationInfo: paginationInfo,
  };
};

const DegreeRequest = {
  getDegrees,
};

export default DegreeRequest;
