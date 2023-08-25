import APILink from "../../../type/APILink";
import JWTUtils from "../../../utils/JWTUtils";
import axios from "axios";

const checkIfUserIsEnrolled = async (
  isUserEnrolledLink: APILink,
): Promise<boolean> => {
  const jwtToken = JWTUtils.getToken();

  if (!isUserEnrolledLink || !jwtToken) {
    return false;
  }

  const response = await axios.get(isUserEnrolledLink.href, {
    headers: {
      Authorization: jwtToken,
    },
  });
  return response.data;
};

export default checkIfUserIsEnrolled;
