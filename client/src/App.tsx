import React, { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "./hooks";
import { BrowserRouter, Route, Routes, useParams } from "react-router-dom";
import * as Pages from "./pages";
import { Header } from "./features/header";
import { fetchBasicLinks } from "./features/links";
import { Footer } from "./features/footer";
import { WINDOW_WIDTH_CUSTOM_BREAKPOINT } from "./utils/Constants";
import { IntegrationSliceActions } from "./store";

export enum AppPaths {
  HOME = "/",
  LOGIN = "/login",
  ARTICLES = "/articles",
  FACULTIES = "/faculties",
  IMAGES = "/images",
  CONTACT = "/about-us/contact",
}

function App() {
  const dispatch = useAppDispatch();
  // const isAuthenticated: boolean = useAppSelector(
  //   (state) => state.auth.data !== null,
  // );

  // Fetch links from API
  useEffect(() => {
    const handleFetchLinks = async () => {
      await dispatch(fetchBasicLinks());
    };

    handleFetchLinks();
  }, [dispatch]);

  // Switch navigation view on resize
  useEffect(() => {
    const handleResize = () => {
      dispatch(
        IntegrationSliceActions.setMobileNavView(
          window.innerWidth <= WINDOW_WIDTH_CUSTOM_BREAKPOINT,
        ),
      );
    };

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  });

  return (
    <BrowserRouter>
      <>
        <Header />
        <Routes>
          <Route path="/" element={<Pages.Home />} />
          <Route path="/login" element={<Pages.Login />} />
          <Route path="/articles" element={<Pages.Articles />} />
          <Route path="/articles/:id" element={<Pages.Article />} />
          <Route path="/research" element={<Pages.Research />} />
          <Route path="/faculties/:facultyId" element={<Pages.Faculty />} />
          <Route
            path="/faculties/:facultyId/teaching-and-studying"
            element={<Pages.TeachingAndStudying />}
          />
          <Route
            path="/faculties/:facultyId/research"
            element={<Pages.Research />}
          />
          <Route
            path="/degree-programmes/:degreeId"
            element={<Pages.Degree />}
          />
          <Route
            path="/degree-programmes"
            element={<Pages.DegreeProgrammes />}
          />
          <Route
            path="/about-us/about-website"
            element={<Pages.AboutTheWebsite />}
          />
        </Routes>
        <Footer />
      </>
      {/*)}*/}
    </BrowserRouter>
  );
}

export default App;
