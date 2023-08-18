import Degree from "../types/Degree";
import Mapper from "../../../type/Mapper";

const mapFromServerData = (data: any): Degree => {
  return {
    id: data.id,
    description: data.description,
    languages: data.languages,
    lengthOfStudy: data.lengthOfStudy,
    tuitionFeePerYear: data.tuitionFeePerYear,
    fieldOfStudy: data.fieldOfStudy,
    title: data.title,
    imageName: data.imageName,
    courses: {
      link: {
        href: data._links.courses.href,
        templated: data._links.courses.templated === true,
      },
    },
    faculty: {
      link: {
        href: data._links.faculty.href,
        templated: data._links.faculty.templated === true,
      },
      name: data.faculty,
    },
  };
};

const mapArrayFromServerData = (data: any[]): Degree[] => {
  return data.map((degree) => mapFromServerData(degree));
};

const DegreeMapper: Mapper<Degree> = {
  mapFromServerData,
  mapArrayFromServerData,
};

export default DegreeMapper;
