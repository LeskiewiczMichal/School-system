import React, { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "./hooks";
import { BrowserRouter, Route, Routes, useParams } from "react-router-dom";
import * as Pages from "./pages";
import { Header } from "./features/header";
import { fetchBasicLinks } from "./features/links";
import { Footer } from "./features/footer";
import { WINDOW_WIDTH_CUSTOM_BREAKPOINT } from "./utils/Constants";
import { IntegrationSliceActions } from "./store";
import { loginWithToken } from "./features/authentication";

export enum AppPaths {
  HOME = "/",
  LOGIN = "/login",
  ARTICLES = "/articles",
  FACULTIES = "/faculties",
  CONTACT = "/about-us/contact",
  COURSES = "/courses",
  TEACHING = "/teaching",
  RESEARCH = "/research",
  DEGREE_PROGRAMMES = "/degree-programmes",
  ACADEMIC_STAFF = "/academic-staff",
  USERS = "/users",
}

function App() {
  const dispatch = useAppDispatch();
  const isAuthenticated: boolean = useAppSelector(
    (state) => state.auth.data !== null,
  );

  // Fetch links from API
  useEffect(() => {
    const handleFetchLinks = async () => {
      await dispatch(fetchBasicLinks());
    };

    handleFetchLinks();
  }, [dispatch]);

  // Auto login
  useEffect(() => {
    const handleAutoLogin = async () => {
      try {
        await dispatch(loginWithToken());
      } catch (error: any) {
        console.log(error);
      }
    };

    handleAutoLogin();
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

          {/* Research and teaching */}
          <Route path={AppPaths.TEACHING} element={<Pages.Teaching />} />
          <Route path={AppPaths.RESEARCH} element={<Pages.Research />} />

          {/* Articles */}
          <Route path="/articles" element={<Pages.Articles />} />
          <Route path="/articles/:id" element={<Pages.Article />} />
          <Route
            path={"/faculties/:facultyId/articles"}
            element={<Pages.Articles />}
          />
          <Route
            path={"/faculties/:facultyId/articles/:id"}
            element={<Pages.Article />}
          />

          {/* Faculty */}
          <Route path="/faculties" element={<Pages.Faculties />} />
          <Route path="/faculties/:facultyId" element={<Pages.Faculty />} />
          <Route
            path="/faculties/:facultyId/teaching-and-studying"
            element={<Pages.TeachingAndStudying />}
          />
          <Route
            path="/faculties/:facultyId/research"
            element={<Pages.Research />}
          />

          {/* Degree */}
          <Route
            path="/degree-programmes/:degreeId"
            element={<Pages.Degree />}
          />
          <Route
            path="/degree-programmes"
            element={<Pages.DegreeProgrammes />}
          />

          {/* Course */}
          <Route path={`${AppPaths.COURSES}`} element={<Pages.Courses />} />
          <Route
            path={`${AppPaths.COURSES}/:courseId`}
            element={<Pages.Course />}
          />
          <Route
            path={`${AppPaths.COURSES}/:courseId/main-page`}
            element={<Pages.CourseMainPage />}
          />

          {/* Users */}
          <Route path={AppPaths.USERS} element={<Pages.Users />} />
          <Route path="/users/:userId" element={<Pages.User />} />
          <Route path="/my-account" element={<Pages.MyAccount />} />
          <Route
            path={AppPaths.ACADEMIC_STAFF}
            element={<Pages.AcademicStaff />}
          />

          {/* About us */}
          <Route
            path="/about-us/about-website"
            element={<Pages.AboutTheWebsite />}
          />
          <Route
            path="/about-us/cookie-management"
            element={<Pages.CookieManagement />}
          />
          <Route path={"/about-us/contact"} element={<Pages.ContactPage />} />
        </Routes>
        <Footer />
      </>
      {/*)}*/}
    </BrowserRouter>
  );
}

export default App;
