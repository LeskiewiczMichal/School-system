import { PayloadAction } from "@reduxjs/toolkit";
import AuthState from "../types/AuthState";

const setAuthUserReducer = (
  state: AuthState,
  action: PayloadAction<AuthState>,
) => {
  state.id = action.payload.id;
  state.email = action.payload.email;
  state.firstName = action.payload.firstName;
  state.lastName = action.payload.lastName;
  state.degree = action.payload.degree;
  state.faculty = action.payload.faculty;
  state.role = action.payload.role;
  state._links = action.payload._links;
};

export default setAuthUserReducer;
