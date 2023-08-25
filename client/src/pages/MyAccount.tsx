import { Sidebar } from "../features/sidebar";
import { useAppSelector } from "../hooks";
import EnumMapper from "../utils/EnumMapper";
import { BasicInformation } from "../features/user";
import ColoredBackgroundWithPhotoOnRight from "../common_components/Card/ColoredBackgroundWithPhotoOnRight";
import CommunityPicture from "./assets/community.webp";
import LinkButtonBorderOnly from "../common_components/button/LinkButtonBorderOnly";
import { useEffect, useState } from "react";
import { FetchCoursesResponse } from "../features/course/services/CourseRequest";
import { Course, CourseRequest } from "../features/course";
import HaveToBeLoggedInInfo from "../common_components/HaveToBeLoggedInInfo";
import { AppPaths } from "../App";

export default function MyAccount() {
  const user = useAppSelector((state) => state.auth.data);
  const links = useAppSelector((state) => state.links);
  const mobileNavBar = useAppSelector(
    (state) => state.integration.mobileNavView,
  );

  // Courses
  const [coursePage, setCoursePage] = useState<number>(0);
  const [courses, setCourses] = useState<Course[]>([]);
  const [coursePaginationInfo, setCoursePaginationInfo] = useState({
    page: 0,
    size: 0,
    totalElements: 0,
    totalPages: 0,
  });

  useEffect(() => {
    const handleFetchCourses = async () => {
      // Prepare the link
      if (!user || !user.courses) {
        return;
      }

      // Call the api
      const response: FetchCoursesResponse = await CourseRequest.getList({
        link: user.courses,
        pagination: { page: coursePage },
      });

      setCourses(response.courses);
      setCoursePaginationInfo(response.paginationInfo);
    };

    handleFetchCourses();
  }, [user, coursePage]);

  // User not authenticated
  if (!user) {
    return (
      <HaveToBeLoggedInInfo
        button={{
          text: "Go to login",
          link: AppPaths.LOGIN,
          color: "brandMain",
        }}
      />
    );
  }

  return (
    <div className={"flex h-full"}>
      {mobileNavBar && <Sidebar />}
      <main
        className={
          "pb-16 border-b sm:px-16 lg:px-0 border-grayscaleMediumDark w-full"
        }
      >
        {/* Basic user info */}
        <section
          className={"flex flex-col px-4 lg:px-32 py-8 mb-8 w-full lg:gap-4"}
        >
          <BasicInformation user={user} />
        </section>

        {/* Courses  */}
        <section className={"px-6 lg:px-16 border-2 border-brandMain"}>
          <h4 className={"my-header text-brandMain mt-10 mb-6"}>
            FACULTIES LIST:
          </h4>

          <div className={"flex flex-col lg:flex-row"}>
            <ul
              className={
                "flex flex-col gap-8 w-full lg:w-1/2 justify-center mb-6"
              }
            >
              {courses.map((course) => (
                <li key={course.id.toString()}>
                  <LinkButtonBorderOnly
                    text={course.title}
                    link={`/courses/${course.id}`}
                    color={"brandMain"}
                    width={"w-full"}
                  />
                </li>
              ))}
            </ul>
          </div>
        </section>
      </main>
    </div>
  );
}
