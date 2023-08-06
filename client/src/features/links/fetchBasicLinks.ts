import { AppThunk } from "../../store";
import axios from "axios";
import LinksState from "./LinksState";
import { setLinks } from "./linksSlice";

const fetchBasicLinks = (): AppThunk => async (dispatch) => {
  return new Promise<void>(async (resolve, reject) => {
    try {
      // Get needed data
      const link = "/api";
      // Send request
      const response = await axios.get(link);

      // Get and map response data
      const links: LinksState = {
        login: {
          href: response.data._links.authenticate.href,
          templated: response.data._links.authenticate.templated === true,
        },
        register: {
          href: response.data._links.register.href,
          templated: response.data._links.register.templated === true,
        },
        users: {
          href: response.data._links.users.href,
          templated: response.data._links.users.templated === true,
        },
        courses: {
          href: response.data._links.courses.href,
          templated: response.data._links.courses.templated === true,
        },
        degrees: {
          href: response.data._links.degrees.href,
          templated: response.data._links.degrees.templated === true,
        },
        faculties: {
          href: response.data._links.faculties.href,
          templated: response.data._links.faculties.templated === true,
        },
        files: {
          href: response.data._links.files.href,
          templated: response.data._links.files.templated === true,
        },
        articles: {
          href: response.data._links.articles.href,
          templated: response.data._links.articles.templated === true,
        },
      };

      // Set links in store
      dispatch(setLinks(links));

      resolve();
    } catch (error: any) {
      console.error(error);
      reject(new Error(error.response.data.message));
    }
  });
};

export default fetchBasicLinks;
