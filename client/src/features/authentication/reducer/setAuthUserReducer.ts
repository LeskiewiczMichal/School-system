import { PayloadAction } from "@reduxjs/toolkit";
import AuthState from "../types/AuthState";

const setAuthUserReducer = (
  state: AuthState,
  action: PayloadAction<AuthState>,
) => {
  state.data = action.payload.data;
  state._links = action.payload._links;
};

export default setAuthUserReducer;
