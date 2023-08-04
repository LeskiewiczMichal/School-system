import APILink from "../type/APILink";
import PaginationParams, { SortDirection } from "../type/PaginationParams";
import axios from "axios";

interface ApiGetParams {
  link: APILink;
  params?: PaginationParams;
}

const performGetRequest = async (args: ApiGetParams) => {
  let {
    link,
    params = {
      sort: ["id", SortDirection.ASC],
      page: 0,
      size: 10,
    },
  } = args;

  let apiLink: string = "";
  if (link.templated) {
    // Process templated link to get the correct href
    if (link.href.endsWith("{?size,page,sort}")) {
      apiLink = link.href.replace(
        "{?size,page,sort}",
        `?size=${params.size}&page=${params.page}&sort=${params.sort[0]},${params.sort[1]}`,
      );
    }
  } else {
    apiLink = link.href;
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
};

export default RequestService;
