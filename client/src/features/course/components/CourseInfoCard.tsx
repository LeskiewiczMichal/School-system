import Course from "../types/Course";
import { Link } from "react-router-dom";
import DegreePageContentInterface from "../../../pages/degree/DegreePageContentInterface";
import EnumMapper from "../../../utils/EnumMapper";
import { useAppSelector } from "../../../hooks";

export interface CourseInfoCardProps {
  course: Course;
  isUserEnrolled: boolean;
}

export default function CourseInfoCard(props: CourseInfoCardProps) {
  const { course, isUserEnrolled } = props;
  const user = useAppSelector((state) => state.auth.data);

  return (
    <section
      className="flex px-6 py-8 lg:mt-10 mb-10 w-full sm:w-10/12 h-[32rem] "
      style={{
        backgroundImage: `url(${require(`../assets/courseInfoCardImages/course-${course.id}.webp`)})`,
        backgroundPosition: "center",
        backgroundSize: "cover",
        // backgroundSize: "contain",
        // backgroundRepeat: "no-repeat",
      }}
    >
      <div className="flex flex-col w-full max-w-3xl text-grayscaleDarkText h-full bg-white p-8">
        <h2 className="text-4xl font-extrabold text-brandMain mb-6">
          {course.title}
        </h2>

        <span>
          <b>Duration:</b> {course.durationInHours} hours
        </span>

        <span>
          <b>Scope:</b>{" "}
          {course.scope
            .map((scope) => EnumMapper.mapScopeToString(scope))
            .join(", ")}
        </span>
        {/*/!*<span><b>ECTS:</b> {course.ects}</span>*!/ // TODO: Add ECTS to the API*/}

        <span>
          Teaching language{" "}
          <b>{EnumMapper.mapLanguageToString(course.language)}</b>
        </span>

        <span>The course is organized by {course.faculty.name}</span>

        <span>Main leader of the course is {course.teacher.name}</span>
      </div>
    </section>
  );
}
