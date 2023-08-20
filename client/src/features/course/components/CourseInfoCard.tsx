import Course from "../types/Course";
import { Link } from "react-router-dom";
import DegreePageContentInterface from "../../../pages/degree/DegreePageContentInterface";
import EnumMapper from "../../../utils/EnumMapper";
import { useAppSelector } from "../../../hooks";
import LinkButtonPrimary from "../../../common_components/button/LinkButtonPrimary";

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
      <div className="flex flex-col w-full max-w-3xl justify-between text-grayscaleDarkText h-full bg-white p-8">
        <div className={"flex flex-col gap-2 mb-4 lg:mb-0"}>
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

          <span>
            <b>ECTS:</b> {course.ECTS} points
          </span>

          <span>
            Teaching language{" "}
            <b>{EnumMapper.mapLanguageToString(course.language)}</b>
          </span>

          <span>
            The course is organized by <b>{course.faculty.name}</b>
          </span>

          <span>
            Main leader of the course is <b>{course.teacher.name}</b>
          </span>
        </div>

        {isUserEnrolled ? (
          <LinkButtonPrimary
            link={`/courses/${course.id}/main-page`}
            text={
              "You are already enrolled in the course. Click here to go to the\n" +
              " course main page"
            }
            fullWidthOnSmallerScreen
          />
        ) : (
          <LinkButtonPrimary
            link={`/courses/${course.id}/enroll`}
            text={"Enroll in the course"}
            fullWidthOnSmallerScreen
          />
        )}
      </div>
    </section>
  );
}
