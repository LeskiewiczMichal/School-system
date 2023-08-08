import { useEffect, useState } from "react";

export default function Sidebar() {
  const [mobileNavView, setMobileNavView] = useState<boolean>(
    window.innerWidth <= 1024,
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

  return mobileNavView ? (
    <div></div>
  ) : (
    <div className="flex flex-col bg-grayscaleLight w-72 px-4 pt-4 border-r h-screen sticky top-0">
      This is a sticky element
      <a href={"#"}>dumb link</a>
      <a className={"mb-16"} href={"#"}>
        dumb link
      </a>
      qwerwer
      <p>sapofm</p>
    </div>
  );
}
