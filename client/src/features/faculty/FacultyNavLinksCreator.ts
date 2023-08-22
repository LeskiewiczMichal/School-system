import { SidebarLinkProps } from "../sidebar/components/SidebarLink";

const createFacultyNavigationLinks = (
  facultyId: string,
): SidebarLinkProps[] => {
  return [
    {
      title: "News",
      redirectUrl: `/faculties/${facultyId}/articles?faculty=${facultyId}`,
    },
    {
      title: "Teaching and Studying",
      redirectUrl: `/faculties/${facultyId}/teaching-and-studying`,
    },
    {
      title: "Research",
      redirectUrl: `/faculties/${facultyId}/research`,
    },
  ];
};

export enum PageType {
  HOME,
  TEACHING_AND_STUDYING,
  DEGREE_PROGRAMMES,
  RESEARCH,
  ARTICLES,
  ACADEMIC_STAFF,
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
      title: "Science and research",
      redirectUrl: `/faculties/${facultyId}/research`,
      active: activePage === PageType.RESEARCH,
    },
    {
      title: "Articles",
      redirectUrl: `/faculties/${facultyId}/articles?faculty=${facultyId}`,
      active: activePage === PageType.ARTICLES,
    },
  ];
};

const FacultyNavLinksCreator = {
  createFacultyNavigationLinks,
  createFacultyNavigationLinksDesktop,
};

export default FacultyNavLinksCreator;
