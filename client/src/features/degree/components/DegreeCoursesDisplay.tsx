import Degree from "../types/Degree";
import MyHeading from "../../../common_components/MyHeading";
import { useEffect } from "react";

export interface DegreeCoursesDisplayProps {
  degree: Degree;
}

export default function DegreeCoursesDisplay(props: DegreeCoursesDisplayProps) {
  const { degree } = props;

  useEffect(() => {
    const handleFetchCourses = async () => {};
  }, [degree]);

  return (
    <section
      className={"flex flex-col bg-brandMain text-white w-full py-4 px-4 gap-4"}
    >
      {/* Heading */}
      <h4 className={"text-xl font-bold"}>Mandatory Courses</h4>

      {/* Courses list */}
    </section>
  );
}
