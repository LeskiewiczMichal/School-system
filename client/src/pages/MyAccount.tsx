import { Sidebar } from "../features/sidebar";
import { useAppDispatch, useAppSelector } from "../hooks";
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
import PaginationButtons from "../common_components/PaginationButtons";
import changePage from "../utils/changePage";
import { setAuthUser } from "../features/authentication/reducer/authSlice";
import { useNavigate } from "react-router-dom";

export default function MyAccount() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
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
        <section className={"flex px-4 lg:px-16 py-8 mb-16 w-full"}>
          <nav>
            <ul>
              <li
                className={
                  "w-full flex items-center justify-between border-4 gap-3 px-4 py-2 border-brandMain text-brandMain hover:border-brandMainActive hover:text-brandMainActive hover:cursor-pointer"
                }
                onClick={() => {
                  dispatch(setAuthUser({ data: null, _links: null }));
                  navigate(AppPaths.LOGIN);
                }}
              >
                Log out
              </li>
            </ul>
          </nav>

          <BasicInformation user={user} />
        </section>

        {/* Courses  */}
        <section
          className={
            "px-6 w-full border-2 border-brandMain flex flex-col lg:flex-row lg:px-16"
          }
        >
          <div className={"flex flex-col justify-center items-center w-full"}>
            <h4 className={"my-header text-brandMain mt-10 mb-6"}>
              Your courses:
            </h4>

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

            {/* Pagination */}
            <PaginationButtons
              paginationInfo={coursePaginationInfo}
              page={coursePage}
              changePage={(direction: "next" | "previous") =>
                changePage(direction, setCoursePage)
              }
            />
          </div>
        </section>
      </main>
    </div>
  );
}
