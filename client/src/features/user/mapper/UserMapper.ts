import UserData from "../UserData";

const mapUserFromServer = (data: any): UserData => ({
  id: data.id,
  email: data.email,
  firstName: data.firstName,
  lastName: data.lastName,
  degree: data.degree,
  faculty: data.faculty,
  role: data.role,
});

const UserMapper = {
  mapUserFromServer,
};

export default UserMapper;
