import Role from "../../../type/Role";
import APILink from "../../../type/APILink";
import NullableString from "../../../type/NullableString";

interface UserData {
  id: bigint;
  email: string;
  firstName: string;
  lastName: string;
  degree: {
    name: string;
    link: APILink;
  } | null;
  faculty: string;
  role: Role;
  courses: APILink;
  teacherDetails: APILink | null;
  profilePictureName: NullableString;
}

export default UserData;
