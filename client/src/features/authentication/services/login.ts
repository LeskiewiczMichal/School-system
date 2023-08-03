import axios from "axios";
import { AppThunk } from "../../../store";
import { UserData, UserMapper } from "../../user";

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
      const response = await axios.post(link, { email, password });

      // Get and map response data
      const { user, _links, token } = response.data;
      const userData: UserData = UserMapper.mapUserFromServer(user);

      // Set token in localstorage
      localStorage.setItem();
    } catch (error) {
      console.error(error);
    }
  };
