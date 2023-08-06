import LinksState from "./LinksState";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState: LinksState = {
  login: null,
  register: null,
  users: null,
  courses: null,
  degrees: null,
  faculties: null,
  files: null,
  articles: null,
};

const linksSlice = createSlice({
  name: "links",
  initialState,
  reducers: {
    setLinks: (state, action: PayloadAction<LinksState>) => {
      state.login = action.payload.login;
      state.register = action.payload.register;
      state.users = action.payload.users;
      state.courses = action.payload.courses;
      state.degrees = action.payload.degrees;
      state.faculties = action.payload.faculties;
      state.files = action.payload.files;
      state.articles = action.payload.articles;
    },
  },
});

export const { setLinks } = linksSlice.actions;
export default linksSlice.reducer;
