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

export default function Header() {
  const dispatch = useAppDispatch();

  // Get the current path
  const location = useLocation();
  const shouldContainSidebar = location.pathname.includes("/articles");
  const isFacultyPage = location.pathname.includes("/faculties");
  const { facultyId } = useParams<{ facultyId: string }>();

  // If it's Faculty's page header
  const [faculty, setFaculty] = useState<Faculty | null>(null);

  useEffect(() => {
    const handleFetchFaculty = async () => {};
  }, [facultyId]);

  // Needed for mobile view
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
            <Link to="/" className="h-12 w-12">
              <LogoWhite className="h-full w-full" />
            </Link>
            <h1 className="text-xl w-full font-bold">Aquila University</h1>
          </div>
          <Link
            to={"/"}
            className={"font-bold text-sm flex items-center gap-1 lg:gap-2"}
          >
            University main page <ArrowRightWhite className={"h-4 w-4"} />
          </Link>
        </section>

        {/* Bottom */}
        <section className="max-h-24 flex flex-auto justify-between items-center py-6 pr-8 bg-white border-b-2 border-primary lg:px-6">
          {/* Navigation */}
          <div className="flex items-center  justify-end gap-4">
            <h1>FACULTY OF </h1>
            {/*<FacultiesDropdown />*/}
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
        </section>
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
