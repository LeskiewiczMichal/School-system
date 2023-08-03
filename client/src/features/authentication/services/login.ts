import axios from "axios";
import { AppThunk } from "../../../store";
import { UserData, UserMapper } from "../../user";
import JWTToken from "../types/JWTToken";
import Role from "../../../type/Role";
import { setAuthUser } from "../reducer/authSlice";

type LoginProps = {
  email: string;
  password: string;
};

const login =
  (props: LoginProps): AppThunk =>
  async (dispatch, getState): Promise<void> => {
    try {
      // Get needed data
      const link = getState().links.login.href;
      const { email, password } = props;

      // Send request
      const response = await axios.post(
        "http://localhost:8080/api/auth/authenticate",
        { email, password },
      );

      // Get and map response data
      const { user, _links, token } = response.data;
      const userData: UserData = UserMapper.mapUserFromServer(user);
      const links = {
        self: {
          href: _links.self.href,
        },
        faculty: {
          href: _links.faculty.href,
        },
        courses: {
          href: _links.courses.href,
        },
        degree: {
          href: userData.role === Role.STUDENT ? _links.degree.href : null,
        },
        teacherDetails: {
          href:
            userData.role === Role.TEACHER ? _links.teacherDetails.href : null,
        },
      };

      dispatch(setAuthUser({ data: userData, _links: links }));
      // Set token in localstorage
      localStorage.setItem(JWTToken.localStorageName, `Bearer ${token}`);
    } catch (error: any) {
      console.error(error);
    }
  };

export default login;
