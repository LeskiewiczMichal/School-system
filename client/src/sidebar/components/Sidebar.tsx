import { useEffect, useState } from "react";
import { ReactComponent as ChevronLeft } from "../../assets/icons/chevron/chevron-left.svg";
import { useAppSelector } from "../../hooks";

export default function Sidebar() {
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

  if (!mobileNavView) {
    return (
      <div className="flex flex-col gap-6 bg-grayscaleLight w-72 px-8 py-10 border-r h-screen sticky top-0">
        <a
          href={"#"}
          className={
            "flex items-center text-xl gap-3 font-bold text-brandMain hover:text-brandMainActive hover:underline"
          }
        >
          <ChevronLeft className={"h-5 w-5"} /> Back
        </a>
        <a
          href={"#"}
          className={
            "text-xl font-bold text-brandMain hover:text-brandMainActive hover:underline"
          }
        >
          News
        </a>
      </div>
    );
  }

  return sidebarMenuActive ? (
    <div className="flex flex-col bg-grayscaleLight w-72 px-4 pt-4 border-r h-screen fixed top-0">
      This is a sticky element
      <a href={"#"}>dumb link</a>
      <a className={"mb-16"} href={"#"}>
        dumb link
      </a>
      qwerwer
      <p>sapofm</p>
    </div>
  ) : null;
}
