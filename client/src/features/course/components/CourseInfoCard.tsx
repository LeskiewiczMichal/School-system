import Course from "../types/Course";

export interface CourseInfoCardProps {
  course: Course;
}

export default function CourseInfoCard(props: CourseInfoCardProps) {
  const { course } = props;

  return (
    <div>
      <h1>CourseInfoCard</h1>
    </div>
  );
}
