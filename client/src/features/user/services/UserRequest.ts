import APILink from "../../../type/APILink";
import {
  OptionalPaginationParams,
  PaginationParams,
  SortDirection,
} from "../../../type/PaginationParams";
import UserData from "../UserData";
import PaginationInfo from "../../../type/PaginationInfo";
import RequestService from "../../../utils/RequestService";
import { UserMapper } from "../index";
import mapPaginationInfoFromServer from "../../../utils/MapPaginationInfoFromServer";

export interface FetchUsersProps {
  link: APILink;
  pagination?: OptionalPaginationParams;
}

export interface FetchUsersResponse {
  users: UserData[];
  paginationInfo: PaginationInfo;
}

/**
 * Fetch users from the API
 *
 * @param props {@link FetchUsersProps} object,
 * containing the link {@link APILink} - link to all users
 *  and pagination {@link OptionalPaginationParams} (optional)
 * @returns Promise of an array of {@link UserData} objects
 */
const getList = async (props: FetchUsersProps): Promise<FetchUsersResponse> => {
  // Prepare the link
  const { link, pagination } = props;

  // Prepare the pagination
  let paginationParams: PaginationParams = {
    page: 0,
    size: 10,
    sort: ["lastName", SortDirection.ASC],
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
  });

  let usersArr: UserData[] = [];
  if (responseData._embedded && responseData._embedded.users) {
    // Convert the response data into users
    usersArr = UserMapper.mapArrayFromServerData(responseData._embedded.users);
  }
  const paginationInfo: PaginationInfo =
    mapPaginationInfoFromServer(responseData);

  return {
    users: usersArr,
    paginationInfo: paginationInfo,
  };
};

export interface FetchSingleUserProps {
  link: APILink;
  id: string;
}

/**
 * Fetch a single user by id from the API
 *
 * @param props {@link FetchSingleUserProps} object, containing the link {@link APILink} and the string - user id
 * @returns Promise of an {@link User} object
 */
const getById = async (props: FetchSingleUserProps): Promise<UserData> => {
  const { link, id } = props;

  const responseData = await RequestService.performGetByIdRequest({
    link: link,
    id: id,
  });

  return UserMapper.mapFromServerData(responseData);
};

const UserRequest = {
  getList,
  getById,
};

export default UserRequest;
