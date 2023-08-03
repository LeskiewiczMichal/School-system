import React from "react";
import { useAppSelector } from "./hooks";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import * as Pages from "./pages";

function App() {
  const isAuthenticated: boolean = useAppSelector(
    (state) => state.auth.data !== null,
  );

  return (
    <BrowserRouter>
      {/*{isAuthenticated ? (*/}
      {/*  <>*/}
      {/*    <div>*/}
      {/*      <h1>qwer</h1>*/}
      {/*    </div>*/}
      {/*  </>*/}
      {/*) : (*/}
      <>
        <Routes>
          <Route path="/" element={<Pages.Home />} />
          <Route path="/login" element={<Pages.Login />} />
        </Routes>
      </>
      {/*)}*/}
    </BrowserRouter>
  );
}

export default App;
