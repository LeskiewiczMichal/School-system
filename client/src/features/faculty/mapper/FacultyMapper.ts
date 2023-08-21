import Faculty from "../types/Faculty";
import Mapper from "../../../type/Mapper";

const mapFromServerData = (data: any): Faculty => {
  return {
    id: data.id,
    name: data.name,
    description: data.description || null,
    articles: {
      href: data._links.articles.href,
      templated: data._links.articles.templated === true,
    },
    courses: {
      href: data._links.courses.href,
      templated: data._links.courses.templated === true,
    },
    degrees: {
      href: data._links.degrees.href,
      templated: data._links.degrees.templated === true,
    },
    students: {
      href: data._links.students.href,
      templated: data._links.students.templated === true,
    },
    teachers: {
      href: data._links.teachers.href,
      templated: data._links.teachers.templated === true,
    },
  };
};

const mapArrayFromServerData = (data: any[]): Faculty[] => {
  return data.map((faculty) => mapFromServerData(faculty));
};

const FacultyMapper: Mapper<Faculty> = {
  mapFromServerData,
  mapArrayFromServerData,
};

export default FacultyMapper;
