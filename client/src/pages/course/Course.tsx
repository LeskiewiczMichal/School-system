import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import {
  Course as CourseType,
  CourseInfoCard,
  CourseRequest,
} from "../../features/course";
import { useAppSelector } from "../../hooks";
import axios from "axios";
import JWTUtils from "../../utils/JWTUtils";

export default function Course() {
  const { courseId } = useParams<{ courseId: string }>();
  const [course, setCourse] = useState<CourseType | null>(null);
  const courseLinks = useAppSelector((state) => state.links.courses);
  const [description, setDescription] = useState<String | null>(null);
  const [isUserEnrolled, setIsUserEnrolled] = useState<boolean>(false);

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

    const handleCheckIfUserIsEnrolled = async () => {
      if (!course) {
        return;
      }

      axios.defaults.headers.common.Authorization = JWTUtils.getToken();
      const response = await axios.get(course.isUserEnrolled.href);
      setIsUserEnrolled(response.data);
    };

    handleFetchCourseDescription();
    handleCheckIfUserIsEnrolled();
  }, [course]);

  if (!course) {
    return <span>Loading</span>;
  }

  return (
    <div>
      <div className={"w-full flex justify-center"}>
        <CourseInfoCard course={course} isUserEnrolled={isUserEnrolled} />
      </div>
    </div>
  );
}
