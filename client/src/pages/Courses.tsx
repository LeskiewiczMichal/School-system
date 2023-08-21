import { useAppSelector } from "../hooks";
import { useEffect, useState } from "react";
import {
  Degree,
  DegreeCard,
  DegreeRequest,
  DegreeSearchForm,
  DegreeTitle,
} from "../features/degree";
import PaginationInfo from "../type/PaginationInfo";
import { GetDegreesResponse } from "../features/degree/services/DegreeRequest";
import { SidebarLinkProps } from "../features/sidebar/components/SidebarLink";
import { Sidebar } from "../features/sidebar";
import MyHeading from "../common_components/MyHeading";
import { AppPaths } from "../App";
import Language from "../type/Language";
import { Course, CourseRequest } from "../features/course";
import { FetchCoursesResponse } from "../features/course/services/CourseRequest";

export default function Courses() {
  const mobileNavView = useAppSelector(
    (state) => state.integration.mobileNavView,
  );
  const links = useAppSelector((state) => state.links);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  // Searching
  const [page, setPage] = useState<number>(0);
  const [faculty, setFaculty] = useState<string | undefined>(undefined);
  const [title, setTitle] = useState<DegreeTitle | undefined>(undefined);
  const [language, setLanguage] = useState<Language | undefined>(undefined);

  // Degree content
  const [courses, setCourses] = useState<Course[]>([]);
  const [paginationInfo, setPaginationInfo] = useState<PaginationInfo>({
    page: 0,
    size: 0,
    totalElements: 0,
    totalPages: 0,
  });

  const handleFetchCourses = async () => {
    // Prepare the link
    if (!links.courses.getCourses || !links.courses.search) {
      return;
    }

    let apiLink = links.courses.search;
    if (faculty || language || title) {
      apiLink = links.courses.search;
    }

    // Call the api
    const response: FetchCoursesResponse = await CourseRequest.getList({
      link: apiLink,
      pagination: { page: page },
      faculty: faculty,
      title: title,
      language: language,
    });

    setCourses(response.courses);
    setPaginationInfo(response.paginationInfo);
  };

  useEffect(() => {
    handleFetchCourses();
  }, [links, page, faculty, title, language]);

  const changePage = (direction: "next" | "previous") => {
    setPage((prevPage) => {
      if (direction === "next") {
        return prevPage + 1;
      } else {
        return prevPage - 1;
      }
    });
  };

  const formChangeHandler = (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    if (event.target.name === "fieldOfStudy") {
      setFieldOfStudy(event.target.value);
    }
    if (event.target.name === "faculty") {
      setFaculty(event.target.value);
      setPage(0);
    }
    if (event.target.name === "title") {
      setTitle(event.target.value as DegreeTitle);
      setPage(0);
    }
  };

  const sidebarLinks: SidebarLinkProps[] = [
    {
      title: "Search for degree programmes",
      redirectUrl: AppPaths.DEGREE_PROGRAMMES,
      active: false,
    },
    {
      title: "Search for courses",
      redirectUrl: AppPaths.COURSES,
      active: true,
    },
  ];

  return (
    <div className={"flex h-full"}>
      <Sidebar links={sidebarLinks} />
      <main className={"h-full w-full flex flex-col lg:px-8 py-8"}>
        {/* Title and text */}
        <h1 className="page-title_h1 px-4 text-brandMainNearlyBlack lg:px-0">
          Our Courses
        </h1>
        <p className={"text-grayscaleDarkText px-4 mb-6 lg:px-0"}>
          Aquila University offers a variety of degree programs. Find the
          perfect fit for you and embark on your educational journey with us.
        </p>
        {/*<DegreeSearchForm*/}
        {/*    fieldOfStudyField={fieldOfStudy}*/}
        {/*    formChangeHandler={formChangeHandler}*/}
        {/*    handleFetchDegrees={handleFetchDegrees}*/}
        {/*    setPage={setPage}*/}
        {/*/>*/}
        {/* Degree programmes */}
        <MyHeading
          heading={`Search results (${paginationInfo.totalElements})`}
        />
        <section className={"flex flex-col gap-4 px-2 sm:px-6 lg:px-0"}>
          {/*{degrees.map((degree) => (*/}
          {/*    <DegreeCard key={degree.id.toString()} degree={degree} />*/}
          {/*))}*/}
        </section>
      </main>
    </div>
  );
}
