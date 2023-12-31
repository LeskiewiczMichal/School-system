import { ReactComponent as LogoWhite } from "../../../assets/logo/logo-white.svg";
import { ReactComponent as MenuIcon } from "../assets/menu.svg";
import { ReactComponent as MenuIconWhite } from "../assets/menu-white.svg";
import { ReactComponent as ArrowRightWhite } from "../../../assets/icons/arrow/arrow-right-white.svg";
import { ReactComponent as UserWhite } from "../../../assets/icons/user/user-white.svg";
import { ReactComponent as UserBrandMain } from "../../../assets/icons/user/user-brandMain.svg";
import FacultiesDropdown from "./FacultiesDropdown";
import { Link, useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import { IntegrationSliceActions } from "../../../store";
import { useAppDispatch, useAppSelector } from "../../../hooks";
import { Faculty } from "../../faculty";
import { Sidebar } from "../../sidebar";
import facultyNamesMap from "../../../type/FacultyNamesMap";
import FacultyNavLinksCreator from "../../faculty/FacultyNavLinksCreator";
import { WINDOW_WIDTH_CUSTOM_BREAKPOINT } from "../../../utils/Constants";
import MainNavLinksCreator from "../utils/MainNavLinksCreator";

export default function Header() {
  const dispatch = useAppDispatch();
  const isAuthenticated: boolean = useAppSelector(
    (state) => state.auth.data !== null,
  );

  // Get the current path
  const location = useLocation();
  const isFacultyPage = location.pathname.includes("/faculties/");

  // Extract faculty id from the path
  let facultyId: string = "";
  if (isFacultyPage) {
    // Split the pathname by '/'
    const pathParts = location.pathname.split("/");

    // Get the content between the second '/' and third '/'
    facultyId = pathParts[2];
  }

  // If it's Faculty's page header
  const [faculty, setFaculty] = useState<Faculty | null>(null);

  // useEffect(() => {
  //   const handleFetchFaculty = async () => {};
  // }, [facultyId]);

  // Needed for mobile view
  const [mobileNavView, setMobileNavView] = useState<boolean>(
    window.innerWidth <= WINDOW_WIDTH_CUSTOM_BREAKPOINT,
  );
  const sidebarMenuActive = useAppSelector(
    (state) => state.integration.sidebarMenuActive,
  );

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth > WINDOW_WIDTH_CUSTOM_BREAKPOINT) {
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

  const facultyHeaderLinks =
    FacultyNavLinksCreator.createFacultyNavigationLinks(facultyId);

  // If it's Faculty's page header
  if (isFacultyPage) {
    return (
      <header className={"z-50"}>
        {/* Top */}
        <section
          className={
            "max-h-12 bg-brandMain text-white flex items-center justify-between lg:justify-start lg:px-6"
          }
        >
          {/* Logo */}
          <div className="flex items-center lg:mr-6">
            <LogoWhite className="h-12 w-12" />

            <h1 className="text-xl w-full font-bold">Aquila University</h1>
          </div>
          <Link
            to={"/"}
            className={
              "font-bold text-sm flex items-center gap-1 lg:gap-2 hover:underline"
            }
          >
            University main page <ArrowRightWhite className={"h-4 w-4"} />
          </Link>
        </section>

        {/* Bottom */}
        <section className="h-28 flex flex-auto justify-between items-center py-6 pr-8 bg-white border-b-2 border-primary lg:px-6">
          {/* Navigation */}
          <div className="flex text-brandMain font-bold items-center w-full gap-4 h-full">
            <Link to={`/faculties/${facultyId}`} className={"pl-6 text-2xl"}>
              {facultyNamesMap.get(facultyId!)}
            </Link>

            {!mobileNavView && (
              <div className={"flex items-center h-full gap-4"}>
                {facultyHeaderLinks.map((link) => (
                  <Link
                    key={link.title}
                    to={link.redirectUrl!}
                    className={
                      "inline-block min-w-fit text-md font-bold text-brandMain align-baseline hover:text-brandMainActive"
                    }
                  >
                    {link.title}
                  </Link>
                ))}

                <FacultiesDropdown facultyHeader brandMain />

                <Link
                  to={isAuthenticated ? "/my-account" : "/login"}
                  className="inline-block min-w-fit text-md font-bold text-brandMain align-baseline hover:text-brandMainActive"
                >
                  <UserBrandMain className={"h-8 w-8"} />
                </Link>
              </div>
            )}

            {/* Hamburger menu to open sidebar */}
            {mobileNavView && (
              <div className={"flex gap-4 ml-auto"}>
                <FacultiesDropdown facultyHeader brandMain />
                <button
                  type="button"
                  className="inline-block ml-auto min-w-fit text-md font-bold text-primary align-baseline hover:text-primaryLighter"
                  onClick={handleClickOpenMenu}
                >
                  <MenuIcon className="h-6 w-6" />
                </button>
              </div>
            )}
          </div>
        </section>
        {mobileNavView && <Sidebar />}
      </header>
    );
  }

  const mainNavLinks = MainNavLinksCreator.createMainNavLinks();

  return (
    <header className="z-50 max-h-24 flex flex-auto justify-between items-center py-6 pr-8 bg-brandMain border-b-2 border-primary">
      <div className="flex  items-center">
        {/* Logo */}
        <Link to="/" className="h-28 w-28">
          <LogoWhite className="h-full w-full" />
        </Link>

        <h1 className="text-xl md:text-2xl w-full font-bold text-white italic">
          Aquila University
        </h1>
      </div>

      {/* Navigation */}
      <div className="flex items-center  justify-end gap-4">
        <FacultiesDropdown />
        {!mobileNavView && (
          <>
            {/* Mapped needed links */}
            {mainNavLinks.map((link) => (
              <Link
                key={link.title}
                to={link.redirectUrl!}
                className={
                  "inline-block min-w-fit text-md font-bold text-white align-baseline hover:underline"
                }
              >
                {link.title}
              </Link>
            ))}

            {/* Only for authenticated user */}
            {isAuthenticated && (
              <>
                <Link
                  to="/users"
                  className="inline-block min-w-fit text-md font-bold text-white align-baseline hover:underline"
                >
                  Users
                </Link>
              </>
            )}

            {/* Login */}
            <Link
              to={isAuthenticated ? "/my-account" : "/login"}
              className="inline-block min-w-fit text-md font-bold text-white align-baseline hover:underline"
            >
              <UserWhite className={"w-8 h-8"} />
            </Link>
          </>
        )}

        {/* Hamburger menu to open sidebar */}
        {mobileNavView && (
          <button
            type="button"
            className="inline-block min-w-fit text-md font-bold text-primary align-baseline hover:text-primaryLighter"
            onClick={handleClickOpenMenu}
          >
            <MenuIconWhite className="h-6 w-6" />
          </button>
        )}
      </div>
    </header>
  );
}
