import UserData from "../types/UserData";
import Mapper from "../../../type/Mapper";

const mapFromServerData = (data: any): UserData => ({
  id: data.id,
  email: data.email,
  firstName: data.firstName,
  lastName: data.lastName,
  degree: data.degree
    ? {
        name: data.degree,
        link: {
          href: data._links.degree.href,
          templated: data._links.degree.templated === true,
        },
      }
    : null,
  faculty: data.faculty,
  role: data.role,
  courses: {
    href: data._links.courses.href,
    templated: data._links.courses.templated === true,
  },
  teacherDetails: data._links.teacherDetails
    ? {
        href: data._links.teacherDetails.href,
        templated: data._links.teacherDetails.templated === true,
      }
    : null,
  profilePictureName: data.profilePictureName,
});

const mapArrayFromServerData = (data: any[]): UserData[] => {
  return data.map((user) => mapFromServerData(user));
};

const UserMapper: Mapper<UserData> = {
  mapFromServerData,
  mapArrayFromServerData,
};

export default UserMapper;
