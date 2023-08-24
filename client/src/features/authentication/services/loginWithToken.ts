import { AppThunk } from "../../../store";
import JWTUtils from "../../../utils/JWTUtils";
import axios from "axios";
import { UserData, UserMapper } from "../../user";
import Role from "../../../type/Role";
import { setAuthUser } from "../reducer/authSlice";
import JWTToken from "../types/JWTToken";
import { Simulate } from "react-dom/test-utils";
import error = Simulate.error;

const loginWithToken =
  (): AppThunk =>
  async (dispatch, getState): Promise<void> => {
    return new Promise<void>(async (resolve, reject) => {
      try {
        // Retrieve token from localstorage
        const jwtToken = JWTUtils.getToken();
        // const link = getState().links.authentication.authenticateWithToken;

        if (!jwtToken) {
          console.log(jwtToken);
          resolve();
          return;
        }
        // if (!link) {
        //   reject(new Error("Login link is not set"));
        //   return;
        // }

        // Set authorization header
        axios.defaults.headers.common.Authorization = jwtToken;

        // Send request
        const response = await axios.post("/api/auth/authenticate/token");

        // Get and map response data
        const { user, _links, token } = response.data;
        const userData: UserData = UserMapper.mapFromServerData(user);
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

export default loginWithToken;
