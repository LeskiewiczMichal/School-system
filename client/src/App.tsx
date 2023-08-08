import React, { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "./hooks";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import * as Pages from "./pages";
import { Header } from "./features/header";
import { fetchBasicLinks } from "./features/links";
import { Footer } from "./features/footer";

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

  return (
    <BrowserRouter>
      <>
        <Header />
        <Routes>
          <Route path="/" element={<Pages.Home />} />
          <Route path="/login" element={<Pages.Login />} />
        </Routes>
        <Footer />
      </>
      {/*)}*/}
    </BrowserRouter>
  );
}

export default App;
