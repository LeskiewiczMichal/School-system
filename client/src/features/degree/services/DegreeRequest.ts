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
  title?: string;
  fieldOfStudy?: string;
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
 * containing the link {@link APILink} - either a search link or a link to all degrees,
 * degree id (optional),
 * title {@link DegreeTitle} (optional) and pagination {@link OptionalPaginationParams} (optional)
 * @returns Promise of an array of {@link Degree} objects
 */
const getList = async (
  props: FetchDegreesProps,
): Promise<GetDegreesResponse> => {
  // Prepare the link
  const { link, faculty, title, fieldOfStudy, pagination } = props;

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
      degreeTitle: title,
    };
  }
  if (fieldOfStudy && fieldOfStudy !== "") {
    params = {
      ...params,
      fieldOfStudy: fieldOfStudy,
    };
  }

  // Prepare the pagination
  let paginationParams: PaginationParams = {
    page: 0,
    size: 10,
    sort: ["fieldOfStudy", SortDirection.ASC],
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

  let degreesArr: Degree[] = [];
  if (responseData._embedded && responseData._embedded.degrees) {
    // Convert the response data into degrees
    degreesArr = DegreeMapper.mapArrayFromServerData(
      responseData._embedded.degrees,
    );
  }
  const paginationInfo: PaginationInfo =
    mapPaginationInfoFromServer(responseData);

  return {
    degrees: degreesArr,
    paginationInfo: paginationInfo,
  };
};

export interface FetchSingleDegreeProps {
  link: APILink;
  id: string;
}

/**
 * Fetch a single degree by id from the API
 *
 * @param props {@link FetchSingleDegreeProps} object, containing the link {@link APILink} and the string - degree id
 * @returns Promise of an {@link Degree} object
 */
const getById = async (props: FetchSingleDegreeProps): Promise<Degree> => {
  const { link, id } = props;

  const responseData = await RequestService.performGetByIdRequest({
    link: link,
    id: id,
  });

  return DegreeMapper.mapFromServerData(responseData);
};

const DegreeRequest = {
  getList,
  getById,
};

export default DegreeRequest;
