import UserData from "../UserData";

const mapUserFromServer = (data: any): UserData => ({
  id: data.id,
  email: data.email,
  firstName: data.firstName,
  lastName: data.lastName,
  degree: data.degree,
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
});

const UserMapper = {
  mapUserFromServer,
};

export default UserMapper;
