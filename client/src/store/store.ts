import { configureStore } from "@reduxjs/toolkit";

import authSlice from "../features/authentication/reducer/authSlice";
import linksSlice from "../features/links/linksSlice";

export const store = configureStore({
  reducer: {
    auth: authSlice,
    links: linksSlice,
  },
});

export default store;
