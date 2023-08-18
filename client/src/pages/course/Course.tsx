import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { Course as CourseType, CourseRequest } from "../../features/course";
import { useAppSelector } from "../../hooks";
import RequestService from "../../utils/RequestService";
import axios from "axios";

export default function Course() {
  const { courseId } = useParams<{ courseId: string }>();
  const [course, setCourse] = useState<CourseType | null>(null);
  const courseLinks = useAppSelector((state) => state.links.courses);
  const [description, setDescription] = useState<String | null>(null);

  useEffect(() => {
    const handleFetchCourse = async () => {
      // Prepare the link
      if (!courseLinks.getById || !courseId) {
        return;
      }

      // Call the API
      const response = await CourseRequest.getById({
        link: courseLinks.getById,
        id: courseId,
      });

      // Set the course
      setCourse(response);
    };
    handleFetchCourse();
  }, [courseId, courseLinks]);

  useEffect(() => {
    const handleFetchCourseDescription = async () => {
      if (!course) {
        return;
      }
      const resposne = await axios.get(course.description.href);
      setDescription(resposne.data);
    };

    handleFetchCourseDescription();
  }, [course]);

  if (!course) {
    return <span>Loading</span>;
  }

  return (
    <div>
      <h1>{course.title}</h1>
    </div>
  );
}
