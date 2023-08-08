import { useEffect, useState } from "react";
import { ReactComponent as ChevronLeft } from "../../assets/icons/chevron/chevron-left.svg";
import { useAppDispatch, useAppSelector } from "../../hooks";
import SidebarLink from "./SidebarLink";
import DarkenLayoutBelowIndexZ from "../../common_components/DarkenLayoutBelowIndexZ";
import { IntegrationSliceActions } from "../../store";

export default function Sidebar() {
  const dispatch = useAppDispatch();
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

  if (!mobileNavView) {
    return (
      <div className="flex flex-col gap-6 bg-grayscaleLight w-72 px-8 py-10 border-r h-screen sticky top-0">
        <a
          href={"/"}
          className={
            "flex items-center text-lg gap-4 font-bold text-brandMainBright hover:underline"
          }
        >
          <ChevronLeft className={"h-6 w-6"} /> Back
        </a>
        <SidebarLink title={"News"} redirectUrl={"/"} active />
        <SidebarLink title={"Science"} redirectUrl={"/"} />
        <SidebarLink title={"Sport"} redirectUrl={"/"} />
        <SidebarLink title={"Events"} redirectUrl={"/"} />
        <SidebarLink title={"Other"} redirectUrl={"/"} />
      </div>
    );
  }

  return sidebarMenuActive ? (
    <>
      <DarkenLayoutBelowIndexZ />
      <div className="z-50 flex flex-col gap-6 bg-grayscaleLight w-72 px-8 py-10 border-l h-screen fixed top-0 right-0">
        <button
          type={"button"}
          className={
            "flex items-center justify-end text-lg gap-3 font-bold text-brandMainLight hover:text-brandMainActive hover:underline"
          }
          onClick={handleClickCloseMenu}
        >
          Close X
        </button>
        <a
          href={"#"}
          className={
            "flex items-center text-lg gap-3 font-bold text-brandMainBright hover:text-brandMainActive hover:underline"
          }
        >
          <ChevronLeft className={"h-5 w-5"} /> Back
        </a>

        <SidebarLink title={"News"} redirectUrl={"/"} active />
        <SidebarLink title={"Science"} redirectUrl={"/"} />
        <SidebarLink title={"Sport"} redirectUrl={"/"} />
        <SidebarLink title={"Events"} redirectUrl={"/"} />
        <SidebarLink title={"Other"} redirectUrl={"/"} />
      </div>
    </>
  ) : null;
}
