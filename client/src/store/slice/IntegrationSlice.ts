import { createSlice } from "@reduxjs/toolkit";
import IntegrationState from "./IntegrationState";

const initialState: IntegrationState = {
  sidebarMenuActive: false,
};

export const integrationSlice = createSlice({
  name: "integration",
  initialState,
  reducers: {
    setSidebarMenuActive: (state, action) => {
      state.sidebarMenuActive = action.payload;
    },
  },
});

export const { setSidebarMenuActive } = integrationSlice.actions;

export default integrationSlice.reducer;
