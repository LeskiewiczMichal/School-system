import NullableString from "../../../type/NullableString";
import { UserData } from "../../user";

export default interface AuthState {
  data: UserData | null;
  _links: {
    self: {
      href: string;
    };
    faculty: {
      href: string;
    };
    courses: {
      href: string;
    };
    degree: {
      href: NullableString;
    };
    teacherDetails: {
      href: NullableString;
    };
  } | null;
}
