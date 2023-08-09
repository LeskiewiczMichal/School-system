import APILink from "../type/APILink";
import { PaginationParams, SortDirection } from "../type/PaginationParams";
import axios from "axios";

interface ApiGetParams {
  link: APILink;
  pagination?: PaginationParams;
  params?: Record<string, any>;
}

const performGetRequest = async (args: ApiGetParams) => {
  let {
    link,
    pagination = {
      sort: ["id", SortDirection.ASC],
      page: 0,
      size: 10,
    },
    params = {},
  } = args;

  // Create API link
  let apiLink: string = "";

  if (link.templated) {
    // Process templated link to get the correct href
    if (link.href.endsWith("{?size,page,sort}")) {
      apiLink = link.href.replace(
        "{?size,page,sort}",
        `?size=${pagination.size}&page=${pagination.page}&sort=${pagination.sort[0]},${pagination.sort[1]}`,
      );
    } else if (link.href.match(/\{\?.*\}$/)) {
      apiLink = link.href.replace(
        /\{\?.*\}$/,
        `?size=${pagination.size}&page=${pagination.page}&sort=${pagination.sort[0]},${pagination.sort[1]}`,
      );
    } else {
      apiLink = link.href;
    }
  } else {
    apiLink = link.href;
  }

  // Append optional parameters to the API link
  let separator = apiLink.includes("?") ? "&" : "?"; // Check if '?' is already present
  for (const key in params) {
    if (params.hasOwnProperty(key)) {
      apiLink += `${separator}${key}=${params[key]}`;
      separator = "&"; // Set the separator to '&' after the first parameter
    }
  }

  return new Promise<any>(async (resolve, reject) => {
    try {
      // Send request
      const response = await axios.get(apiLink);
      resolve(response.data);
    } catch (error: any) {
      console.error(error);
      reject(new Error(error.response.data.message));
    }
  });
};

interface ApiGetByIdParams {
  link: APILink;
  id?: string;
  params?: Record<string, any>;
}

const performGetByIdRequest = async (props: ApiGetByIdParams) => {
  const { link, id, params = {} } = props;

  // Create API link
  let apiLink: string = link.href;

  if (link.templated && id) {
    // Process templated link to get the correct href
    if (link.href.match(/\{id\}/g)) {
      apiLink = link.href.replace("{id}", id);
    }
  }

  // Append optional parameters to the API link
  let separator = apiLink.includes("?") ? "&" : "?"; // Check if '?' is already present
  for (const key in params) {
    if (params.hasOwnProperty(key)) {
      apiLink += `${separator}${key}=${params[key]}`;
      separator = "&"; // Set the separator to '&' after the first parameter
    }
  }

  return new Promise<any>(async (resolve, reject) => {
    try {
      // Send request
      const response = await axios.get(apiLink);
      resolve(response.data);
    } catch (error: any) {
      console.error(error);
      reject(new Error(error.response.data.message));
    }
  });
};

const RequestService = {
  performGetRequest,
  performGetByIdRequest,
};

export default RequestService;
