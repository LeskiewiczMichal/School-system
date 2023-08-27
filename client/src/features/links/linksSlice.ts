import LinksState from "./LinksState";
import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState: LinksState = {
  authentication: {
    login: null,
    register: null,
    authenticateWithToken: null,
  },
  users: {
    getUsers: null,
    getById: null,
    search: null,
  },
  courses: {
    getCourses: null,
    getById: null,
    search: null,
  },
  degrees: {
    getDegrees: null,
    getById: null,
    search: null,
  },
  faculties: {
    getFaculties: null,
    getById: null,
  },
  files: null,
  articles: {
    getArticles: null,
    getById: null,
    search: null,
  },
  mail: {
    sendContactEmail: null,
  },
};

const linksSlice = createSlice({
  name: "links",
  initialState,
  reducers: {
    setLinks: (state, action: PayloadAction<LinksState>) => {
      state.authentication = action.payload.authentication;
      state.users = action.payload.users;
      state.courses = action.payload.courses;
      state.degrees = action.payload.degrees;
      state.faculties = action.payload.faculties;
      state.files = action.payload.files;
      state.articles = action.payload.articles;
      state.mail = action.payload.mail;
    },
  },
});

export const { setLinks } = linksSlice.actions;
export default linksSlice.reducer;
