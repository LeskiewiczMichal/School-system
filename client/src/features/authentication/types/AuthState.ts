import { Role } from "../../../type/Role";

export default interface AuthState {
  id: bigint | null;
  email: string | null;
  firstName: string | null;
  lastName: string | null;
  degree: string | null;
  faculty: string | null;
  role: Role | null;
}
