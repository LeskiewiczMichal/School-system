import { useParams } from "react-router-dom";
import {useEffect, useState} from "react";
import {Course as CourseType, CourseRequest} from "../../features/course";
import {useAppSelector} from "../../hooks";

export default function Course() {
  const { courseId } = useParams<{ courseId: string }>();
  const [course, setCourse] = useState<CourseType | null>(null);
  const courseLinks = useAppSelector((state) => state.links.courses);

  useEffect(() => {
    const handleFetchCourse = async () => {
        // Prepare the link
        if (!courseLinks.getById || !courseId) {
            return;
        }

        // Call the API
        const response = await CourseRequest.({
            link: courseLinks.getById,
            id: courseId,
        });

        // Set the course
        setCourse(response);
    }
  }, []);

  return (
    <div>
      <h1>Not implemented yet</h1>
    </div>
  );
}
