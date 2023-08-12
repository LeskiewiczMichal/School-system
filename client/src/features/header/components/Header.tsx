import { ReactComponent as Logo } from "../../../assets/logo/logo.svg";
import { ReactComponent as LogoWhite } from "../../../assets/logo/logo-white.svg";
import { ReactComponent as MenuIcon } from "../assets/menu.svg";
import { ReactComponent as ArrowRightWhite } from "../../../assets/icons/arrow/arrow-right-white.svg";
import FacultiesDropdown from "./FacultiesDropdown";
import { Link, useLocation, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { IntegrationSliceActions } from "../../../store";
import { useAppDispatch, useAppSelector } from "../../../hooks";
import { Faculty } from "../../faculty";
import { Sidebar } from "../../sidebar";
import { SidebarLinkProps } from "../../sidebar/components/SidebarLink";
import facultyNamesMap from "../../../type/FacultyNamesMap";
import FacultyNavLinksCreator from "../../faculty/FacultyNavLinksCreator";
import { WINDOW_WIDTH_CUSTOM_BREAKPOINT } from "../../../utils/Constants";

export default function Header() {
  const dispatch = useAppDispatch();

  // Get the current path
  const location = useLocation();
  const shouldContainSidebar = location.pathname.includes("/articles");
  const isFacultyPage = location.pathname.includes("/faculties");

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
        <section className="max-h-24 flex flex-auto justify-between items-center py-6 pr-8 bg-white border-b-2 border-primary lg:px-6">
          {/* Navigation */}
          <div className="flex text-brandMain font-bold items-center w-full gap-12">
            <Link to={`/faculties/${facultyId}`} className={"pl-6 text-2xl"}>
              {facultyNamesMap.get(facultyId!)}
            </Link>

            {!mobileNavView && (
              <div className={"flex items-center h-full gap-4"}>
                {facultyHeaderLinks.map((link) => (
                  <Link
                    to={link.redirectUrl!}
                    className={
                      "inline-block min-w-fit text-md font-bold text-brandMain align-baseline hover:text-brandMainActive"
                    }
                  >
                    {link.title}
                  </Link>
                ))}
                <FacultiesDropdown facultyHeader />
              </div>
            )}
            {/* Login */}
            {/*<Link*/}
            {/*  to="/login"*/}
            {/*  className="inline-block min-w-fit text-md font-bold text-primary align-baseline hover:text-primaryLighter"*/}
            {/*>*/}
            {/*  Sign In*/}
            {/*</Link>*/}
            {/* Hamburger menu to open sidebar */}
            {mobileNavView && (
              <button
                type="button"
                className="inline-block ml-auto min-w-fit text-md font-bold text-primary align-baseline hover:text-primaryLighter"
                onClick={handleClickOpenMenu}
              >
                <MenuIcon className="h-6 w-6" />
              </button>
            )}
          </div>
        </section>
        {mobileNavView && <Sidebar links={facultyHeaderLinks} />}
      </header>
    );
  }

  return (
    <header className="z-50 max-h-24 flex flex-auto justify-between items-center py-6 pr-8 bg-white border-b-2 border-primary">
      <div className="flex  items-center">
        {/* Logo */}
        <Link to="/" className="h-28 w-28">
          <Logo className="h-full w-full" />
        </Link>
        <h1 className="text-xl md:text-2xl w-full font-bold text-primary italic">
          Aquila University
        </h1>
      </div>

      {/* Navigation */}
      <div className="flex items-center  justify-end gap-4">
        <FacultiesDropdown />
        {/* Login */}
        <Link
          to="/login"
          className="inline-block min-w-fit text-md font-bold text-primary align-baseline hover:text-primaryLighter"
        >
          Sign In
        </Link>
        {/* Hamburger menu to open sidebar */}
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
