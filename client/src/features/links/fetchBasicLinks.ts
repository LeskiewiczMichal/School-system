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
        },
        register: {
          href: response.data._links.register.href,
        },
        users: {
          href: response.data._links.users.href,
        },
        courses: {
          href: response.data._links.courses.href,
        },
        degrees: {
          href: response.data._links.degrees.href,
        },
        faculties: {
          href: response.data._links.faculties.href,
        },
        files: {
          href: response.data._links.files.href,
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
