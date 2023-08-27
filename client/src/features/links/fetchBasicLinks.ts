import { AppThunk } from "../../store";
import axios from "axios";
import LinksState from "./LinksState";
import { setLinks } from "./linksSlice";

const fetchBasicLinks = (): AppThunk => async (dispatch) => {
  return new Promise<void>(async (resolve, reject) => {
    try {
      // Get needed data
      const link = `${process.env.REACT_APP_SERVER_URL}/api`;
      // Send request
      const response = await axios.get(link);

      // Get and map response data
      const links: LinksState = {
        authentication: {
          login: {
            href: response.data._links.authenticate.href,
            templated: response.data._links.authenticate.templated === true,
          },
          register: {
            href: response.data._links.register.href,
            templated: response.data._links.register.templated === true,
          },
          authenticateWithToken: {
            href: response.data._links.authenticateWithToken.href,
            templated:
              response.data._links.authenticateWithToken.templated === true,
          },
        },
        users: {
          getUsers: {
            href: response.data._links.users[0].href,
            templated: response.data._links.users[0].templated === true,
          },
          getById: {
            href: response.data._links.users[1].href,
            templated: response.data._links.users[1].templated === true,
          },
          search: {
            href: response.data._links.users[2].href,
            templated: response.data._links.users[2].templated === true,
          },
        },
        courses: {
          getCourses: {
            href: response.data._links.courses[0].href,
            templated: response.data._links.courses[0].templated === true,
          },
          getById: {
            href: response.data._links.courses[1].href,
            templated: response.data._links.courses[1].templated === true,
          },
          search: {
            href: response.data._links.courses[2].href,
            templated: response.data._links.courses[2].templated === true,
          },
        },
        degrees: {
          getDegrees: {
            href: response.data._links.degrees[0].href,
            templated: response.data._links.degrees[0].templated === true,
          },
          getById: {
            href: response.data._links.degrees[1].href,
            templated: response.data._links.degrees[1].templated === true,
          },
          search: {
            href: response.data._links.degrees[2].href,
            templated: response.data._links.degrees[2].templated === true,
          },
        },
        faculties: {
          getFaculties: {
            href: response.data._links.faculties[0].href,
            templated: response.data._links.faculties[0].templated === true,
          },
          getById: {
            href: response.data._links.faculties[1].href,
            templated: response.data._links.faculties[1].templated === true,
          },
        },
        files: {
          href: response.data._links.files.href,
          templated: response.data._links.files.templated === true,
        },
        articles: {
          getArticles: {
            href: response.data._links.articles[0].href,
            templated: response.data._links.articles[0].templated === true,
          },
          getById: {
            href: response.data._links.articles[1].href,
            templated: response.data._links.articles[1].templated === true,
          },
          search: {
            href: response.data._links.articles[2].href,
            templated: response.data._links.articles[2].templated === true,
          },
        },
        mail: {
          sendContactEmail: {
            href: response.data._links.sendContactEmail.href,
            templated: response.data._links.sendContactEmail.templated === true,
          },
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
