import Course from "../types/Course";
import { Link } from "react-router-dom";
import DegreePageContentInterface from "../../../pages/degree/DegreePageContentInterface";
import EnumMapper from "../../../utils/EnumMapper";

export interface CourseInfoCardProps {
  course: Course;
}

export default function CourseInfoCard(props: CourseInfoCardProps) {
  const { course } = props;

  return (
    <section
      className="flex px-6 py-8 lg:mt-10 mb-10 w-10/12 h-[32rem] "
      style={{
        backgroundImage: `url(${require(`../assets/courseInfoCardImages/course-${course.id}.webp`)})`,
        backgroundPosition: "center",
        backgroundSize: "cover",
        // backgroundSize: "contain",
        // backgroundRepeat: "no-repeat",
      }}
    >
      <div className="flex flex-col w-full max-w-3xl text-grayscaleDarkText h-full bg-white p-8">
        <h2 className="text-4xl font-extrabold text-brandMain">
          {course.title}
        </h2>
        <span>
          <b>Duration:</b> {course.durationInHours} hours
        </span>
        {/*/!*<span><b>ECTS:</b> {course.ects}</span>*!/ // TODO: Add ECTS to the API*/}

        <span>
          <b>Language:</b> {EnumMapper.mapLanguageToString(course.language)}
        </span>
        <span>
          <b>Scope:</b>{" "}
          {course.scope
            .map((scope) => EnumMapper.mapScopeToString(scope))
            .join(", ")}
        </span>
      </div>
    </section>
  );
}
