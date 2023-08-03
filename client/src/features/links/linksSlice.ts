import LinksState from "./LinksState";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState: LinksState = {
  login: {
    href: "",
  },
  register: {
    href: "",
  },
  logout: {
    href: "",
  },
  users: {
    href: "",
  },
  courses: {
    href: "",
  },
  degrees: {
    href: "",
  },
  faculties: {
    href: "",
  },
  files: {
    href: "",
  },
};

const linksSlice = createSlice({
  name: "links",
  initialState,
  reducers: {
    setLinks: (state, action: PayloadAction<LinksState>) => {
      state.login = action.payload.login;
      state.register = action.payload.register;
      state.logout = action.payload.logout;
      state.users = action.payload.users;
      state.courses = action.payload.courses;
      state.degrees = action.payload.degrees;
      state.faculties = action.payload.faculties;
      state.files = action.payload.files;
    },
  },
});

export const { setLinks } = linksSlice.actions;
export default linksSlice.reducer;
