import { useAppSelector } from "../hooks";
import { useEffect, useState } from "react";
import PaginationInfo from "../type/PaginationInfo";
import { SidebarLinkProps } from "../features/sidebar/components/SidebarLink";
import { Sidebar } from "../features/sidebar";
import MyHeading from "../common_components/MyHeading";
import { AppPaths } from "../App";
import Language from "../type/Language";
import { Course, CourseRequest, CourseSearchForm } from "../features/course";
import { FetchCoursesResponse } from "../features/course/services/CourseRequest";
import LoadingSpinner from "../common_components/LoadingSpinner";
import { Link } from "react-router-dom";
import { ReactComponent as ArrowUpRightBrandMain } from "../assets/icons/arrow/arrow-up-right-brandMain.svg";
import PaginationButtons from "../common_components/PaginationButtons";

export default function Courses() {
  const mobileNavView = useAppSelector(
    (state) => state.integration.mobileNavView,
  );
  const links = useAppSelector((state) => state.links);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  // Searching
  const [page, setPage] = useState<number>(0);
  const [faculty, setFaculty] = useState<string>("");
  const [title, setTitle] = useState<string>("");
  const [language, setLanguage] = useState<Language | string>("");

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

    let apiLink = links.courses.getCourses;
    if (faculty !== "" || language !== "" || title !== "") {
      apiLink = links.courses.search;
    }

    // Call the api
    const response: FetchCoursesResponse = await CourseRequest.getList({
      link: apiLink,
      pagination: { page: page, size: 20 },
      faculty: faculty,
      title: title,
      language: language,
    });

    setCourses(response.courses);
    setPaginationInfo(response.paginationInfo);
    setIsLoading(false);
  };

  useEffect(() => {
    handleFetchCourses();
  }, [links, page, faculty, language]);

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
    if (event.target.name === "language") {
      setLanguage(event.target.value as Language);
      setPage(0);
    }
    if (event.target.name === "faculty") {
      setFaculty(event.target.value);
      setPage(0);
    }
    if (event.target.name === "title") {
      setTitle(event.target.value);
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
          Explore the rich tapestry of educational opportunities at Aquila
          University, where a myriad of captivating courses await your
          discovery. With our extensive selection, you're sure to find the
          perfect fit that aligns with your interests and aspirations.
        </p>

        <CourseSearchForm
          titleField={title}
          formChangeHandler={formChangeHandler}
          handleFetchCourses={handleFetchCourses}
          setPage={setPage}
        />

        <MyHeading
          heading={`Search results (${paginationInfo.totalElements})`}
        />

        {/* Courses display */}
        {isLoading && <LoadingSpinner />}
        {courses.length !== 0 && !isLoading && (
          <ul
            className={
              "grid grid-cols-1 gap-x-12 gap-y-8 px-2 sm:px-6 lg:px-0 list-disc justify-center items-center ml-8 md:grid-cols-2"
            }
          >
            {courses.map((course) => (
              <li>
                <Link
                  to={`/courses/${course.id}`}
                  key={course.id.toString()}
                  className={
                    "text-xl text-brandMain font-bold hover:text-brandMainActive hover:underline"
                  }
                >
                  <div>
                    {course.title} by the {course.faculty.name}{" "}
                    <ArrowUpRightBrandMain className={"inline h-6 w-6"} />
                  </div>
                </Link>
              </li>
            ))}
          </ul>
        )}
        {courses.length === 0 && !isLoading && (
          <span>No courses matching your requirements were found.</span>
        )}

        {/* Pagination buttons */}
        <PaginationButtons
          paginationInfo={paginationInfo}
          page={page}
          changePage={changePage}
        />
      </main>
    </div>
  );
}
