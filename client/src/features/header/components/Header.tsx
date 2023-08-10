import { ReactComponent as Logo } from "../../../assets/logo/logo.svg";
import { ReactComponent as MenuIcon } from "../assets/menu.svg";
import FacultiesDropdown from "./FacultiesDropdown";
import { Link, useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import { IntegrationSliceActions } from "../../../store";
import { useAppDispatch, useAppSelector } from "../../../hooks";

export default function Header() {
  const location = useLocation();

  // Example conditional rendering logic
  const shouldContainSidebar = location.pathname.includes("/articles");
  const isFacultyPage = location.pathname.includes("/faculties");

  const dispatch = useAppDispatch();
  const [mobileNavView, setMobileNavView] = useState<boolean>(
    window.innerWidth <= 1024,
  );
  const sidebarMenuActive = useAppSelector(
    (state) => state.integration.sidebarMenuActive,
  );

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth > 1024) {
        setMobileNavView(false);
        dispatch(IntegrationSliceActions.setSidebarMenuActive(false));
      } else {
        setMobileNavView(true);
      }
    };

    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  const handleClickOpenMenu = () => {
    dispatch(IntegrationSliceActions.setSidebarMenuActive(!sidebarMenuActive));
  };

  return (
    <header className="z-50 max-h-20 flex flex-auto justify-between items-center py-6 pr-8 bg-white border-b-2 border-primary">
      <div className="flex  items-center">
        <Link to="/" className="h-28 w-28">
          <Logo className="h-full w-full" />
        </Link>
        <h1 className="text-xl md:text-2xl w-full font-bold text-primary italic">
          Aquila University
        </h1>
      </div>

      <div className="flex items-center  justify-end gap-4">
        <FacultiesDropdown />
        {/*<a*/}
        {/*  className="inline-block text-md font-bold text-primary align-baseline hover:text-primaryLighter"*/}
        {/*  href="#"*/}
        {/*>*/}
        {/*  About*/}
        {/*</a>*/}
        <Link
          to="/login"
          className="inline-block min-w-fit text-md font-bold text-primary align-baseline hover:text-primaryLighter"
        >
          Sign In
        </Link>
        {mobileNavView && shouldContainSidebar && (
          <button
            type="button"
            className="inline-block min-w-fit text-md font-bold text-primary align-baseline hover:text-primaryLighter"
            onClick={handleClickOpenMenu}
          >
            <MenuIcon className="h-6 w-6" />
          </button>
        )}
      </div>
    </header>
  );
}
