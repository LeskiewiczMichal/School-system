import { AppThunk } from "../../store";
import axios from "axios";

const fetchBasicLinks = (): AppThunk => async (dispatch) => {
  return new Promise<void>(async (resolve, reject) => {
    try {
      // Get needed data
      const link = "/api";
      // Send request
      const response = await axios.get(link);

      console.log(response.data);
      resolve();
    } catch (error: any) {
      console.error(error);
      reject(new Error(error.response.data.message));
    }
  });
};

export default fetchBasicLinks;
