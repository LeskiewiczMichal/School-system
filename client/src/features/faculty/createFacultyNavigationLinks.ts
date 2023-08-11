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

export default createFacultyNavigationLinks;
