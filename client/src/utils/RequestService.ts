import APILink from "../type/APILink";
import PaginationParams, { SortDirection } from "../type/PaginationParams";
import axios from "axios";

interface ApiGetParams {
  link: APILink;
  params: PaginationParams;
}

const performGetRequest = (args: ApiGetParams) => {
  let {
    link,
    params = {
      sort: ["id", SortDirection.ASC],
      page: 0,
      size: 10,
    },
  } = args;

  if (link.templated) {
    // Process templated link to get the correct href
    if (link.href.endsWith("{?size,page,sort}")) {
      link.href = link.href.replace(
        "{?size,page,sort}",
        `?size=${params.size}&page=${params.page}&sort=${params.sort[0]},${params.sort[1]}`,
      );
    }
  }

  return new Promise<any>(async (resolve, reject) => {
    try {
      // Send request
      const response = await axios.get(link.href);
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
