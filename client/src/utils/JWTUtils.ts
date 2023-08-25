import JWTToken from "../features/authentication/types/JWTToken";

const getToken = () => {
  return localStorage.getItem(JWTToken.localStorageName);
};

const setToken = (token: string) => {
  localStorage.setItem(JWTToken.localStorageName, token);
};

const removeToken = () => {
  localStorage.removeItem(JWTToken.localStorageName);
};

const JWTUtils = {
  getToken,
  setToken,
  removeToken,
};

export default JWTUtils;
