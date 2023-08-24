import JWTToken from "../features/authentication/types/JWTToken";

const getToken = () => {
  return localStorage.getItem(JWTToken.localStorageName);
};

const JWTUtils = {
  getToken,
};

export default JWTUtils;
