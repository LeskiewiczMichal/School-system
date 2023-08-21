import { SidebarLinkProps } from "../../sidebar/components/SidebarLink";

const createMainNavLinks = (): SidebarLinkProps[] => {
  return [
    {
      title: "Articles",
      redirectUrl: `/articles`,
    },
    {
      title: "Research",
      redirectUrl: `/research`,
    },
    {
      title: "Degrees and Courses",
      redirectUrl: `/degree-programmes`,
    },
  ];
};

const MainNavLinksCreator = {
  createMainNavLinks,
};

export default MainNavLinksCreator;
