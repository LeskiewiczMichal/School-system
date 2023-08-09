import APILink from "../../type/APILink";

interface LinksState {
  login: APILink | null;
  register: APILink | null;
  users: APILink | null;
  courses: APILink | null;
  degrees: APILink | null;
  faculties: APILink | null;
  files: APILink | null;
  articles: {
    getArticles: APILink | null;
    getById: APILink | null;
    search: APILink | null;
  };
}

export default LinksState;
