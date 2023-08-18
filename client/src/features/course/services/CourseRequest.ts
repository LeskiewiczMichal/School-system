import Course from "../types/Course";
import APILink from "../../../type/APILink";
import DegreeTitle from "../../degree/types/DegreeTitle";
import {
  OptionalPaginationParams,
  PaginationParams,
  SortDirection,
} from "../../../type/PaginationParams";
import RequestService from "../../../utils/RequestService";
import { CourseMapper } from "../index";
import PaginationInfo from "../../../type/PaginationInfo";
import mapPaginationInfoFromServer from "../../../utils/MapPaginationInfoFromServer";
import Degree from "../../degree/types/Degree";

export type FetchCoursesProps = {
  link: APILink;
  faculty?: string;
  title?: string;
  degreeId?: string;
  pagination?: OptionalPaginationParams;
};

export interface FetchCoursesResponse {
  courses: Course[];
  paginationInfo: PaginationInfo;
}

const getList = async (
  props: FetchCoursesProps,
): Promise<FetchCoursesResponse> => {
  // Prepare the link
  const { link, faculty, title, degreeId, pagination } = props;

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
      title: title,
    };
  }
  if (degreeId && degreeId !== "") {
    params = {
      ...params,
      degree: degreeId,
    };
  }

  // Prepare the pagination
  let paginationParams: PaginationParams = {
    page: 0,
    size: 10,
    sort: ["title", SortDirection.ASC],
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

  let coursesArr: Course[] = [];
  if (responseData._embedded && responseData._embedded.courses) {
    // Convert the response data into degrees
    coursesArr = CourseMapper.mapArrayFromServerData(
      responseData._embedded.degrees,
    );
  }
  const paginationInfo: PaginationInfo =
    mapPaginationInfoFromServer(responseData);

  return {
    courses: coursesArr,
    paginationInfo: paginationInfo,
  };
};

const CourseRequest = {
  getList,
};

export default CourseRequest;
