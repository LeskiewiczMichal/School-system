const getToken = () => {
  return localStorage.getItem("token");
};

const JWTUtils = {
  getToken,
};

export default JWTUtils;
