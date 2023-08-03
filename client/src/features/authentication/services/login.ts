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
    return new Promise<void>(async (resolve, reject) => {
      try {
        // Get needed data
        const link = getState().links.login.href;
        const { email, password } = props;

        // Send request
        const response = await axios.post(link, { email, password });

        // Get and map response data
        const { user, _links, token } = response.data;
        const userData: UserData = UserMapper.mapUserFromServer(user);
        const links = {
          self: {
            href: user._links.self.href,
          },
          faculty: {
            href: user._links.faculty.href,
          },
          courses: {
            href: user._links.courses.href,
          },
          degree: {
            href:
              userData.role === Role.STUDENT ? user._links.degree.href : null,
          },
          teacherDetails: {
            href:
              userData.role === Role.TEACHER
                ? user._links.teacherDetails.href
                : null,
          },
        };

        dispatch(setAuthUser({ data: userData, _links: links }));
        // Set token in localstorage
        localStorage.setItem(JWTToken.localStorageName, `Bearer ${token}`);
        resolve();
      } catch (error: any) {
        console.error(error);
        reject(new Error(error.response.data.message));
      }
    });
  };

export default login;
