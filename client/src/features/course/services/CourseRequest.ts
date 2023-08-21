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
  language?: string;
  pagination?: OptionalPaginationParams;
};

export interface FetchCoursesResponse {
  courses: Course[];
  paginationInfo: PaginationInfo;
}

/**
 * Fetch courses from the API (using search or not)
 *
 * @param props {@link FetchCoursesProps} object,
 * containing the link {@link APILink} - either a search link or a link to all courses,
 * degreeId id (optional),
 * title (string, optional) and pagination {@link OptionalPaginationParams} (optional)
 * @returns Promise of an array of {@link Course} objects
 */
const getList = async (
  props: FetchCoursesProps,
): Promise<FetchCoursesResponse> => {
  // Prepare the link
  const { link, faculty, title, language, pagination } = props;

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
  if (language) {
    params = {
      ...params,
      language: language,
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
      responseData._embedded.courses,
    );
  }
  const paginationInfo: PaginationInfo =
    mapPaginationInfoFromServer(responseData);

  return {
    courses: coursesArr,
    paginationInfo: paginationInfo,
  };
};

export interface FetchSingleCourseProps {
  link: APILink;
  id: string;
}

/**
 * Fetch a single course by id from the API
 *
 * @param props {@link FetchSingleCourseProps} object, containing the link {@link APILink} and the string - course id
 * @returns Promise of an {@link Course} object
 */
const getById = async (props: FetchSingleCourseProps): Promise<Course> => {
  const { link, id } = props;

  const responseData = await RequestService.performGetByIdRequest({
    link: link,
    id: id,
  });

  return CourseMapper.mapFromServerData(responseData);
};

const CourseRequest = {
  getList,
  getById,
};

export default CourseRequest;
