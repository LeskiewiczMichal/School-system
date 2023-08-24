import APILink from "../../type/APILink";

interface LinksState {
  authentication: {
    login: APILink | null;
    register: APILink | null;
    authenticateWithToken: APILink | null;
  };
  users: {
    getUsers: APILink | null;
    getById: APILink | null;
    search: APILink | null;
  };
  courses: {
    getCourses: APILink | null;
    getById: APILink | null;
    search: APILink | null;
  };
  degrees: {
    getDegrees: APILink | null;
    getById: APILink | null;
    search: APILink | null;
  };
  faculties: {
    getFaculties: APILink | null;
    getById: APILink | null;
  };
  files: APILink | null;
  articles: {
    getArticles: APILink | null;
    getById: APILink | null;
    search: APILink | null;
  };
}

export default LinksState;
