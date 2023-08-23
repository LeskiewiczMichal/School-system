import { useAppSelector } from "../hooks";
import { Sidebar } from "../features/sidebar";
import { SidebarLinkProps } from "../features/sidebar/components/SidebarLink";

export default function Users() {
  const user = useAppSelector((state) => state.auth.data);

  const sidebarLinks: SidebarLinkProps[] = [
    {
      title: "Academic Staff",
      redirectUrl: "/academic-staff",
    },
    {
      title: "All Users",
      redirectUrl: "/users",
      active: true,
    },
  ];

  return (
    <div className={"flex h-full"}>
      <Sidebar links={sidebarLinks} />
      <main
        className={
          "pb-16 border-b sm:px-16 lg:px-0 border-grayscaleMediumDark w-full"
        }
      >
        <section
          className={"flex flex-col px-4 lg:px-32 py-8 mb-4 w-full lg:gap-4"}
        ></section>
      </main>
    </div>
  );
}
