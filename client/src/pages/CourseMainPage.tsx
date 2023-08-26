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
import { ReactComponent as ArrowRight } from "../assets/icons/arrow/arrow-up-right-brandMain.svg";
import { File, FileRequest } from "../features/files";
import PaginationInfo from "../type/PaginationInfo";
import { FetchFilesResponse } from "../features/files/services/FileRequest";
import MyHeading from "../common_components/MyHeading";
import { UserData, UserListCard } from "../features/user";
import UserRequest from "../features/user/services/UserRequest";
import PaginationButtons from "../common_components/PaginationButtons";
import changePage from "../utils/changePage";

export default function CourseMainPage() {
  const { courseId } = useParams<{ courseId: string }>();
  const mobileNavBar = useAppSelector(
    (state) => state.integration.mobileNavView,
  );
  const [isLoading, setIsLoading] = useState<boolean>(true);

  // Current user
  const user = useAppSelector((state) => state.auth.data);
  const links = useAppSelector((state) => state.links);
  const [isUserEnrolled, setIsUserEnrolled] = useState<boolean>(false);

  // Students
  const [students, setStudents] = useState<UserData[]>([]);
  const [studentsPage, setStudentsPage] = useState<number>(0);
  const [studentsPaginationInfo, setStudentsPaginationInfo] =
    useState<PaginationInfo>({
      size: 0,
      totalElements: 0,
      totalPages: 0,
      page: 0,
    });

  // Course
  const [course, setCourse] = useState<CourseType | null>(null);
  const [description, setDescription] = useState<string | null>(null);

  // Files
  const [files, setFiles] = useState<File[]>([]);
  const [filesPage, setFilesPage] = useState<number>(0);
  const [filesPaginationInfo, setFilesPaginationInfo] =
    useState<PaginationInfo>({
      size: 0,
      totalElements: 0,
      totalPages: 0,
      page: 0,
    });

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

    const handleFetchCourseFiles = async (fetchFilesLink: APILink) => {
      const response: FetchFilesResponse = await FileRequest.getList({
        link: fetchFilesLink,
        pagination: {
          size: 15,
          page: filesPage,
        },
      });

      setFiles(response.files);
      setFilesPaginationInfo(response.paginationInfo);
    };

    const handleFetchStudents = async (fetchStudentsLink: APILink) => {
      const response = await UserRequest.getList({
        link: fetchStudentsLink,
        pagination: {
          size: 12,
          page: studentsPage,
        },
      });
      setStudents(response.users);
      setStudentsPaginationInfo(response.paginationInfo);
    };

    handleCheckIfUserIsEnrolled();
    handleFetchCourseDescription(course.description);
    handleFetchCourseFiles(course.files);
    handleFetchStudents(course.students.getStudents);
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
          "pb-16 border-b px-4 sm:px-16 lg:px-0 border-grayscaleMediumDark w-full"
        }
      >
        {/* Header */}
        <h1
          className={
            "page-title_h1 w-full px-4 sm:px-0 flex justify-center text-brandMain items-center mt-8 self-center"
          }
        >
          {course.title}
        </h1>

        <section className={"w-full flex flex-col lg:flex-row"}>
          <div
            className={
              "px-8 py-8 border-2 mb-8 border-brandMain text-brandMain grid grid-cols-2 flex-grow-0 gap-3 lg:flex lg:flex-col lg:w-1/4 lg:mb-0 lg:border-l-0"
            }
          >
            {/* Teacher */}
            <div className={"flex flex-col"}>
              <span>Teacher:</span>
              <Link
                to={`/users/${course.teacher.link.href.split("/").pop()}`}
                className={"font-bold flex gap-2 items-center hover:underline"}
              >
                {course.teacher.name} <ArrowRight className={"w-6 h-6"} />
              </Link>
            </div>

            {/* Faculty */}
            <div className={"flex flex-col"}>
              <span>Teacher:</span>
              <Link
                to={`/faculties/${course.faculty.link.href.split("/").pop()}`}
                className={"font-bold flex gap-2 items-center hover:underline"}
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

          {/* Course description */}
          {description && (
            <div
              className={
                "flex flex-col lg:px-16 justify-center lg:w-full py-6 border-brandMain lg:border-t-2 border-b-2"
              }
            >
              <h4 className={"text-brandMain font-bold text-2xl mb-2"}>
                Course description:
              </h4>
              <p
                className={"text-grayscaleDarkText"}
                dangerouslySetInnerHTML={{ __html: marked.marked(description) }}
              ></p>
            </div>
          )}
        </section>

        {/* Course files */}
        <section className={"py-8 lg:px-8 text-brandMain"}>
          <h4 className="my-header mb-8 ml-4 lg:ml-0 text-brandMain">
            Course files
          </h4>

          <div className={"flex flex-col lg:grid lg:grid-cols-2"}>
            {files.map((file) => (
              <span
                key={file.id.toString()}
                className={"hover:underline hover:cursor-pointer"}
                onClick={() => FileRequest.downloadFile(file.linkToFile, file)}
              >
                {file.fileName}
              </span>
            ))}
          </div>
          <PaginationButtons
            paginationInfo={filesPaginationInfo}
            page={filesPage}
            changePage={(direction: "next" | "previous") =>
              changePage(direction, setFilesPage)
            }
          />
        </section>

        {/* Course students */}
        <section
          className={"py-8 lg:px-8 border-t-2 border-brandMain text-brandMain"}
        >
          <h4 className="my-header mb-8 ml-4 lg:ml-0 text-brandMain">
            Course students
          </h4>

          {students.length !== 0 && !isLoading && (
            <div
              className={
                "grid grid-cols-2 md:grid-cols-4 xl:grid-cols-6 gap-4 px-4 lg:px-0"
              }
            >
              {students.map((student) => (
                <UserListCard user={student} />
              ))}
            </div>
          )}
          <PaginationButtons
            paginationInfo={studentsPaginationInfo}
            page={studentsPage}
            changePage={(direction: "next" | "previous") =>
              changePage(direction, setStudentsPage)
            }
          />
        </section>
      </main>
    </div>
  );
}
