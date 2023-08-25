import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import {
  checkIfUserIsEnrolled,
  Course as CourseType,
  CourseRequest,
} from "../features/course";
import { useAppSelector } from "../hooks";
import HaveToBeLoggedInInfo from "../common_components/HaveToBeLoggedInInfo";
import { AppPaths } from "../App";
import LoadingSpinnerPage from "./LoadingSpinnerPage";
import { Sidebar } from "../features/sidebar";
import APILink from "../type/APILink";
import * as marked from "marked";
import axios from "axios";
import EnumMapper from "../utils/EnumMapper";
import { ReactComponent as ArrowRight } from "../assets/icons/arrow/arrow-right-white.svg";

export default function CourseMainPage() {
  const { courseId } = useParams<{ courseId: string }>();
  const mobileNavBar = useAppSelector(
    (state) => state.integration.mobileNavView,
  );
  const [isLoading, setIsLoading] = useState<boolean>(true);

  const user = useAppSelector((state) => state.auth.data);
  const links = useAppSelector((state) => state.links);
  const [isUserEnrolled, setIsUserEnrolled] = useState<boolean>(false);

  // Course
  const [course, setCourse] = useState<CourseType | null>(null);
  const [description, setDescription] = useState<string | null>(null);

  useEffect(() => {
    const handleFetchCourse = async () => {
      // Prepare the link
      if (!links.courses.getById || !courseId) {
        return;
      }

      // Call the API
      const response = await CourseRequest.getById({
        link: links.courses.getById,
        id: courseId,
      });

      // Set the course
      setCourse(response);
    };

    handleFetchCourse();
  }, [courseId, links]);

  useEffect(() => {
    if (!course) {
      return;
    }

    const handleCheckIfUserIsEnrolled = async () => {
      setIsUserEnrolled(await checkIfUserIsEnrolled(course.isUserEnrolled));
      setIsLoading(false);
    };

    const handleFetchCourseDescription = async (
      fetchDescriptionLink: APILink,
    ) => {
      const resposne = await axios.get(fetchDescriptionLink.href);
      setDescription(resposne.data);
    };

    handleCheckIfUserIsEnrolled();
    handleFetchCourseDescription(course.description);
  }, [course]);

  if (isLoading || !course) {
    return <LoadingSpinnerPage />;
  }

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

  if (!isUserEnrolled) {
    return (
      <HaveToBeLoggedInInfo text={"You are not enrolled in this course"} />
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
        {/* Header */}
        <h1
          className={
            "page-title_h1 w-full px-4 sm:px-0 flex justify-center text-brandMain items-center py-10 self-center"
          }
        >
          {course.title}
        </h1>

        <section className={"w-full flex flex-col lg:flex-row"}>
          <div
            className={
              "px-8 py-8 border-2 mb-8 border-brandMain text-brandMain grid grid-cols-2 flex-grow-0 gap-3 h-fit lg:flex lg:flex-col lg:w-1/4 lg:mb-0"
            }
          >
            {/* Teacher */}
            <div className={"flex flex-col"}>
              <span>Teacher:</span>
              <Link
                to={`/users/`}
                className={"font-bold flex gap-3 items-center hover:underline"}
              >
                {course.teacher.name} <ArrowRight className={"w-6 h-6"} />
              </Link>
            </div>

            {/* Faculty */}
            <div className={"flex flex-col"}>
              <span>Teacher:</span>
              <Link
                to={`/faculties/`}
                className={"font-bold flex gap-3 items-center hover:underline"}
              >
                {course.faculty.name} <ArrowRight className={"w-6 h-6"} />
              </Link>
            </div>

            {/* Duration */}
            <div className={"flex flex-col"}>
              <span>Duration:</span>
              <span className={"font-bold"}>
                {course.durationInHours} hours
              </span>
            </div>

            {/* Scope */}
            <div className={"flex flex-col"}>
              <span>Scope:</span>
              <span className={"font-bold"}>
                {course.scope
                  .map((scope) => EnumMapper.mapScopeToString(scope))
                  .join(", ")}
              </span>
            </div>

            {/* Language */}
            <div className={"flex flex-col"}>
              <span>Language:</span>
              <span className={"font-bold"}>
                {EnumMapper.mapLanguageToString(course.language)}
              </span>
            </div>

            {/* ECTS */}
            <div className={"flex flex-col"}>
              <span>ECTS:</span>
              <span className={"font-bold"}>{course.ECTS} points</span>
            </div>
          </div>

          {description && (
            <p
              className={"text-grayscaleDarkText pl-8 lg:w-2/3"}
              dangerouslySetInnerHTML={{ __html: marked.marked(description) }}
            ></p>
          )}
        </section>
      </main>
    </div>
  );
}
