import { createSlice } from "@reduxjs/toolkit";
import IntegrationState from "./IntegrationState";

const initialState: IntegrationState = {
  mobileNavBarIconActive: false,
};

export const integrationSlice = createSlice({
  name: "integration",
  initialState,
  reducers: {
    setMobileNavBarIconActive: (state, action) => {
      state.mobileNavBarIconActive = action.payload;
    },
  },
});

export const { setMobileNavBarIconActive } = integrationSlice.actions;

export default integrationSlice.reducer;
