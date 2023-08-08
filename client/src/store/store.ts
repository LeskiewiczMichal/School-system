import { configureStore } from "@reduxjs/toolkit";

import authSlice from "../features/authentication/reducer/authSlice";
import linksSlice from "../features/links/linksSlice";
import integrationSlice from "./slice/IntegrationSlice";

export const store = configureStore({
  reducer: {
    auth: authSlice,
    links: linksSlice,
    integration: integrationSlice,
  },
});

export default store;
