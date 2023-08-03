import { createSlice } from "@reduxjs/toolkit";
import AuthState from "../types/AuthState";
import setAuthUserReducer from "./setAuthUserReducer";

const initialState: AuthState = {
  data: {
    id: null,
    email: null,
    firstName: null,
    lastName: null,
    degree: null,
    faculty: null,
    role: null,
  },
  _links: null,
};

export const authSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setAuthUser: setAuthUserReducer,
  },
});

export const { setAuthUser } = authSlice.actions;

export default authSlice.reducer;
