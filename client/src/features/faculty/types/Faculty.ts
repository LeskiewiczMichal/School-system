import APILink from "../../../type/APILink";

interface Faculty {
  id: number;
  name: string | null;
  description: string;
  articles: APILink;
  courses: APILink;
  degrees: APILink;
  students: APILink;
  teachers: APILink;
}

export default Faculty;
