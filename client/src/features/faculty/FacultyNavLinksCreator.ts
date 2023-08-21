import { SidebarLinkProps } from "../sidebar/components/SidebarLink";

const createFacultyNavigationLinks = (
  facultyId: string,
): SidebarLinkProps[] => {
  return [
    {
      title: "News",
      redirectUrl: `/articles?faculty=${facultyId}`,
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
      title: "Academic staff",
      redirectUrl: `/faculties/${facultyId}/academic-staff`,
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
    {
      title: "Academic staff",
      redirectUrl: `/faculties/${facultyId}/academic-staff`,
      active: activePage === PageType.ACADEMIC_STAFF,
    },
  ];
};

const FacultyNavLinksCreator = {
  createFacultyNavigationLinks,
  createFacultyNavigationLinksDesktop,
};

export default FacultyNavLinksCreator;
