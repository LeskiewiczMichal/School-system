import Degree from "../types/Degree";
import MyHeading from "../../../common_components/MyHeading";
import { ReactComponent as ArrowRightWhite } from "../../../assets/icons/arrow/arrow-right-white.svg";
import { useEffect, useState } from "react";
import { FetchCoursesResponse } from "../../course/services/CourseRequest";
import { Course, CourseRequest } from "../../course";
import PaginationInfo from "../../../type/PaginationInfo";
import { Link } from "react-router-dom";
import { AppPaths } from "../../../App";

export interface DegreeCoursesDisplayProps {
  degree: Degree;
}

export default function DegreeCoursesDisplay(props: DegreeCoursesDisplayProps) {
  const { degree } = props;
  const [courses, setCourses] = useState<Course[]>([]);
  const [paginationInfo, setPaginationInfo] = useState<PaginationInfo>({
    page: 0,
    size: 0,
    totalElements: 0,
    totalPages: 0,
  });

  useEffect(() => {
    const handleFetchCourses = async () => {
      // Prepare the link
      if (!degree.courses.link) {
        return;
      }
      let apiLink = degree.courses.link;

      // Call the api
      const response: FetchCoursesResponse = await CourseRequest.getList({
        link: apiLink,
        pagination: { size: 50 },
      });

      setCourses(response.courses);
      setPaginationInfo(response.paginationInfo);
    };

    handleFetchCourses();
  }, [degree]);

  return (
    <section
      className={"flex flex-col bg-brandMain text-white w-full py-4 px-4 gap-4"}
    >
      {/* Heading */}
      <h4 className={"text-xl font-bold"}>Mandatory Courses</h4>

      {/* Courses list */}
      {courses.map((course) => (
        <Link
          to={`${AppPaths.COURSES}/${course.id}`}
          key={course.id.toString()}
          className={"flex items-center gap-2 hover:underline"}
        >
          {course.title} <ArrowRightWhite className={"w-6 h-6"} />
        </Link>
      ))}
    </section>
  );
}
