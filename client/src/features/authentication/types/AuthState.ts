import { Role } from "../../../type/Role";
import { Link } from "../../../type/Link";

export default interface AuthState {
  data: {
    id: bigint | null;
    email: string | null;
    firstName: string | null;
    lastName: string | null;
    degree: string | null;
    faculty: string | null;
    role: Role | null;
  };
  _links: {
    self: {
      href: Link;
    };
    faculty: {
      href: Link;
    };
    courses: {
      href: Link;
    };
    degree: {
      href: Link;
    };
    teacherDetails: {
      href: Link;
    };
  } | null;
}
