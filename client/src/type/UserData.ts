import Role from "./Role";

interface UserData {
  id: bigint;
  email: string;
  firstName: string;
  lastName: string;
  degree: string | null;
  faculty: string;
  role: Role;
}

export default UserData;
