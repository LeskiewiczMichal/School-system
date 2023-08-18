import Course from "../types/Course";
import { Link } from "react-router-dom";
import DegreePageContentInterface from "../../../pages/degree/DegreePageContentInterface";

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
      {/*<img*/}
      {/*  src={require(`../assets/courseInfoCardImages/course-${course.id}.webp`)}*/}
      {/*  className="w-full lg:w-1/2 lg:order-2"*/}
      {/*  alt="Faculty greeting"*/}
      {/*/>*/}
      <div className="flex flex-col w-full h-full bg-white justify-center pl-8 pr-8  gap-4 pt-8">
        <h2 className="text-5xl font-extrabold text-brandMain">
          {course.title}
        </h2>
        <p>{course.durationInHours}</p>
      </div>
    </section>
  );
}
