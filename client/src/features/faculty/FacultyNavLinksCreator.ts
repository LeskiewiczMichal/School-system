import { SidebarLinkProps } from "../sidebar/components/SidebarLink";

const createFacultyNavigationLinks = (
  facultyId: string,
): SidebarLinkProps[] => {
  return [
    {
      title: "News",
      redirectUrl: `/faculties/${facultyId}/news`,
    },
    {
      title: "Teaching and Studying",
      redirectUrl: `/faculties/${facultyId}/teaching-and-studying`,
    },
    {
      title: "Research",
      redirectUrl: `/faculties/${facultyId}/research`,
    },
    {
      title: "Degree Programmes",
      redirectUrl: `/faculties/${facultyId}/degree-programmes`,
    },
  ];
};

export enum PageType {
  HOME,
  TEACHING_AND_STUDYING,
  DEGREE_PROGRAMMES,
  RESEARCH,
  ARTICLES,
}

const createFacultyNavigationLinksDesktop = (
  facultyId: string,
  activePage: PageType,
): SidebarLinkProps[] => {
  return [
    {
      title: "Home",
      redirectUrl: `/faculties/${facultyId}`,
      active: activePage === PageType.HOME,
    },
    {
      title: "Education",
      redirectUrl: `/faculties/${facultyId}/teaching-and-studying`,
      active: activePage === PageType.TEACHING_AND_STUDYING,
    },
    {
      title: "Our degrees",
      redirectUrl: `/faculties/${facultyId}/degree-programmes`,
      active: activePage === PageType.DEGREE_PROGRAMMES,
    },
    {
      title: "Science and research",
      redirectUrl: `/faculties/${facultyId}/research`,
      active: activePage === PageType.RESEARCH,
    },
    {
      title: "Articles",
      redirectUrl: `/faculties/${facultyId}/articles`,
      active: activePage === PageType.ARTICLES,
    },
  ];
};

const FacultyNavLinksCreator = {
  createFacultyNavigationLinks,
  createFacultyNavigationLinksDesktop,
};

export default FacultyNavLinksCreator;
