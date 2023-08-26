import APILink from "../../../type/APILink";
import axios from "axios";
import {
  OptionalPaginationParams,
  PaginationParams,
  SortDirection,
} from "../../../type/PaginationParams";
import PaginationInfo from "../../../type/PaginationInfo";
import RequestService from "../../../utils/RequestService";
import { FileMapper } from "../index";
import mapPaginationInfoFromServer from "../../../utils/MapPaginationInfoFromServer";
import File from "../types/File";

export interface FetchFilesProps {
  link: APILink;
  pagination?: OptionalPaginationParams;
}

export interface FetchFilesResponse {
  files: File[];
  paginationInfo: PaginationInfo;
}

/**
 * Fetch files from the API
 *
 * @param props {@link FetchFilesProps} object,
 * containing the link {@link APILink} - either a search link or a link to all files,
 * and pagination {@link OptionalPaginationParams} (optional)
 * @returns Promise of an array of {@link File} objects
 */
const getList = async (props: FetchFilesProps): Promise<FetchFilesResponse> => {
  const { link, pagination } = props;

  // Prepare the pagination
  let paginationParams: PaginationParams = {
    page: 0,
    size: 10,
    sort: ["file_name", SortDirection.ASC],
  };
  if (pagination) {
    paginationParams = {
      page: pagination.page ? pagination.page : paginationParams.page,
      size: pagination.size ? pagination.size : paginationParams.size,
      sort: pagination.sort ? pagination.sort : paginationParams.sort,
    };
  }

  // Call the API
  const response = await RequestService.performGetRequest({
    link: link,
    params: paginationParams,
  });

  let files: File[] = [];
  if (response._embedded && response._embedded.files) {
    // Convert the response data into files
    files = FileMapper.mapArrayFromServerData(response._embedded.files);
  }
  const paginationInfo: PaginationInfo = mapPaginationInfoFromServer(response);

  return {
    files: files,
    paginationInfo: paginationInfo,
  };
};

const downloadFile = async (fileLink: APILink) => {
  try {
    const response = await axios.get(fileLink.href, {
      responseType: "blob",
    });

    const blob = new Blob([response.data]);
    const url = window.URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.setAttribute(
      "download",
      response.headers["content-disposition"].split("=")[1],
    );
    document.body.appendChild(link);
    link.click();

    URL.revokeObjectURL(url);
    link.remove();
  } catch (error) {
    console.error("Error downloading file:", error);
  }
};

const FileRequest = {
  getList,
  downloadFile,
};

export default FileRequest;
