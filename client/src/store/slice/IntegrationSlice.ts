import { createSlice } from "@reduxjs/toolkit";
import IntegrationState from "./IntegrationState";
import { WINDOW_WIDTH_CUSTOM_BREAKPOINT } from "../../utils/Constants";

const initialState: IntegrationState = {
  sidebarMenuActive: false,
  mobileNavView: window.innerWidth <= WINDOW_WIDTH_CUSTOM_BREAKPOINT,
};

export const integrationSlice = createSlice({
  name: "integration",
  initialState,
  reducers: {
    setSidebarMenuActive: (state, action) => {
      state.sidebarMenuActive = action.payload;
    },
    setMobileNavView: (state, action) => {
      state.mobileNavView = action.payload;
    },
  },
});

export const { setSidebarMenuActive, setMobileNavView } =
  integrationSlice.actions;

export default integrationSlice.reducer;
