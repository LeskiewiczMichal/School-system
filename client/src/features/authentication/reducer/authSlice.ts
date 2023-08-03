import { createSlice } from "@reduxjs/toolkit";
import AuthState from "../types/AuthState";
import setAuthUserReducer from "./setAuthUserReducer";

const initialState: AuthState = {
  data: null,
  _links: null,
};

export const authSlice = createSlice({
  name: "authUser",
  initialState,
  reducers: {
    setAuthUser: setAuthUserReducer,
  },
});

export const { setAuthUser } = authSlice.actions;

export default authSlice.reducer;
