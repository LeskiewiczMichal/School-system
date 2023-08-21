import { useEffect, useState } from "react";
import { ReactComponent as ChevronLeft } from "../../../assets/icons/chevron/chevron-left.svg";
import { useAppDispatch, useAppSelector } from "../../../hooks";
import SidebarLink, { SidebarLinkProps } from "./SidebarLink";
import DarkenLayoutBelowIndexZ from "../../../common_components/DarkenLayoutBelowIndexZ";
import { IntegrationSliceActions } from "../../../store";
import SidebarButton, { SidebarButtonProps } from "./SidebarButton";
import { Link, useLocation, useNavigate } from "react-router-dom";
import FacultyNavLinksCreator from "../../faculty/FacultyNavLinksCreator";
import MainNavLinksCreator from "../../header/utils/MainNavLinksCreator";

export interface SidebarProps {
  links?: SidebarLinkProps[];
  buttons?: SidebarButtonProps[];
  backButtonLink?: string;
}

export default function Sidebar(props: SidebarProps) {
  const navigate = useNavigate();
  const { links, buttons, backButtonLink } = props;
  const dispatch = useAppDispatch();
  const isAuthenticated: boolean = useAppSelector(
    (state) => state.auth.data !== null,
  );

  const location = useLocation();
  const isFacultiesPageSidebar = location.pathname.includes("/faculties/");
  // Extract faculty id from the path
  let facultyId: string = "";
  if (isFacultiesPageSidebar) {
    // Split the pathname by '/'
    const pathParts = location.pathname.split("/");

    // Get the content between the second '/' and third '/'
    facultyId = pathParts[2];
  }

  // Mobile view support for sidebar
  const [mobileNavView, setMobileNavView] = useState<boolean>(
    window.innerWidth <= 1024,
  );
  const sidebarMenuActive = useAppSelector(
    (state) => state.integration.sidebarMenuActive,
  );

  // Handles closing the mobile nav when the window is resized
  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth > 1024) {
        setMobileNavView(false);
      } else {
        setMobileNavView(true);
      }
    };

    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  const handleClickCloseMenu = () => {
    dispatch(IntegrationSliceActions.setSidebarMenuActive(false));
  };

  const handleClickBack = () => {
    if (backButtonLink) {
      navigate(backButtonLink);
    } else {
      window.history.back();
    }
  };

  if (!mobileNavView) {
    return (
      <div className="flex flex-col flex-none gap-6 bg-grayscaleLight w-72 px-8 py-10 border-r border-grayscaleMediumDark  h-screen sticky top-0">
        {/*  Back button */}
        <button
          onClick={handleClickBack}
          className={
            "flex items-center text-lg gap-4 font-bold text-brandMainBright hover:underline"
          }
        >
          <ChevronLeft className={"h-6 w-6"} /> Back
        </button>

        {/* Buttons */}
        {buttons &&
          buttons.map((button) => (
            <SidebarButton
              key={button.text}
              text={button.text}
              active={button.active}
              onClick={button.onClick}
            />
          ))}

        {/*  Links */}
        {links &&
          links.map((link) => (
            <SidebarLink
              key={link.title}
              title={link.title}
              redirectUrl={link.redirectUrl}
              active={link.active}
            />
          ))}
      </div>
    );
  }

  let sidebarLinks: SidebarLinkProps[] =
    MainNavLinksCreator.createMainNavLinks();
  if (isFacultiesPageSidebar) {
    sidebarLinks =
      FacultyNavLinksCreator.createFacultyNavigationLinks(facultyId);
  }

  return sidebarMenuActive ? (
    <>
      <DarkenLayoutBelowIndexZ />
      <div className="z-50 flex flex-col gap-6 bg-grayscaleLight w-72 px-8 py-10 border-l h-screen fixed top-0 right-0">
        {/* Close menu button */}
        <button
          type={"button"}
          className={
            "flex items-center justify-end text-lg gap-3 font-bold text-brandMainLight hover:text-brandMainActive hover:underline"
          }
          onClick={handleClickCloseMenu}
        >
          Close X
        </button>

        {/*  Back button */}
        <button
          onClick={handleClickBack}
          className={
            "flex items-center text-lg gap-4 font-bold text-brandMainBright hover:underline"
          }
        >
          <ChevronLeft className={"h-6 w-6"} /> Back
        </button>

        {/* Buttons */}
        {buttons &&
          buttons.map((button) => (
            <SidebarButton
              key={button.text}
              text={button.text}
              active={button.active}
              onClick={button.onClick}
            />
          ))}

        {/* Links */}
        {links &&
          links.map((link) => (
            <SidebarLink
              key={link.title}
              title={link.title}
              redirectUrl={link.redirectUrl}
              active={link.active}
            />
          ))}

        {/* Navigation */}
        <h4 className={"mt-4 text-brandMainLight font-bold text-lg"}>
          Navigation:
        </h4>
        {/* Account */}
        {isAuthenticated ? (
          <SidebarLink title={"My account"} redirectUrl={"/my-account"} />
        ) : (
          <SidebarLink title={"Sign in"} redirectUrl={"/login"} />
        )}
        {sidebarLinks.map((link) => (
          <SidebarLink
            key={link.title}
            title={link.title}
            redirectUrl={link.redirectUrl}
            active={link.active}
          />
        ))}
      </div>
    </>
  ) : null;
}
