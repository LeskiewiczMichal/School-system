import Course from "./types/Course";
import Mapper from "../../type/Mapper";

const mapFromServerData = (data: any): Course => {
  return {
    id: data.id,
    title: data.title,
    durationInHours: data.durationInHours,
    language: data.language,
    scope: data.scope,
    faculty: {
      name: data.faculty,
      link: {
        href: data._links.faculty.href,
        templated: data._links.faculty.templated === true,
      },
    },
    teacher: {
      name: data.teacher,
      link: {
        href: data._links.teacher.href,
        templated: data._links.teacher.templated === true,
      },
    },
    students: {
      href: data._links.students.href,
      templated: data._links.students.templated === true,
    },
    files: {
      href: data._links.files.href,
      templated: data._links.files.templated === true,
    },
    description: {
      href: data._links.description.href,
      templated: data._links.description.templated === true,
    },
  };
};

const mapArrayFromServerData = (data: any[]): Course[] => {
  return data.map((course) => mapFromServerData(course));
};

const CourseMapper: Mapper<Course> = {
  mapFromServerData,
  mapArrayFromServerData,
};

export default CourseMapper;
