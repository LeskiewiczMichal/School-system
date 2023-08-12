import Degree from "../types/Degree";

const mapFromServerData = (data: any): Degree => {
  return {
    id: data.id,
    faculty: {
      link: {
        href: data._links.faculty.href,
        templated: data._links.faculty.templated === true,
      },
      name: data.faculty,
    },
    fieldOfStudy: data.fieldOfStudy,
    title: data.title,
    courses: {
      link: {
        href: data._links.courses.href,
        templated: data._links.courses.templated === true,
      },
    },
  };
};

const DegreeMapper = {
  mapFromServerData,
};

export default DegreeMapper;
