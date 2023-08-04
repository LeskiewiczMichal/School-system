import APILink from "../../type/APILink";

interface LinksState {
  login: APILink | null;
  register: APILink | null;
  users: APILink | null;
  courses: APILink | null;
  degrees: APILink | null;
  faculties: APILink | null;
  files: APILink | null;
}

export default LinksState;
