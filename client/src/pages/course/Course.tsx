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
import * as marked from "marked";
import MyHeading from "../../common_components/MyHeading";
import LoadingSpinnerPage from "../LoadingSpinnerPage";

export default function Course() {
  const { courseId } = useParams<{ courseId: string }>();
  const [course, setCourse] = useState<CourseType | null>(null);
  const courseLinks = useAppSelector((state) => state.links.courses);
  const [description, setDescription] = useState<string | null>(null);
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
      const jwtToken = JWTUtils.getToken();

      if (!course || !jwtToken) {
        return;
      }

      const response = await axios.get(course.isUserEnrolled.href, {
        headers: {
          Authorization: jwtToken,
        },
      });
      setIsUserEnrolled(response.data);
    };

    handleFetchCourseDescription();
    handleCheckIfUserIsEnrolled();
  }, [course]);

  if (!course) {
    return <LoadingSpinnerPage />;
  }

  return (
    <div>
      <div className={"w-full flex flex-col items-center"}>
        <CourseInfoCard course={course} isUserEnrolled={isUserEnrolled} />

        <div className={"px-6 lg:px-32 my-10 border-t border-brandMain"}>
          <h4 className="my-header mb-4 mt-12 text-brandMainNearlyBlack">
            Enrollment
          </h4>

          <p className={"text-grayscaleDarkText flex flex-col gap-3 sm:mr-8"}>
            <span>
              <b>This is a collaborative program:</b> University Aquila welcomes
              participation from both its degree students and Open University
              students in a shared teaching experience. However, please note
              that only a limited number of Open University students can be
              accommodated in this course.
            </span>

            <span>
              Students interested in joining the course can easily enroll by
              clicking on the "Enrollment" button located at the top of the
              course webpage during the designated enrollment period. The
              enrollment process includes online payment of the applicable study
              fees. Keep in mind that once enrolled, the commitment is binding.
            </span>
          </p>
        </div>

        <div className={"px-6 lg:px-32 my-10"}>
          <h4 className="my-header mb-4 text-brandMainNearlyBlack">
            About course
          </h4>

          {description && (
            <p
              className={"text-grayscaleDarkText flex flex-col gap-3 sm:mr-8"}
              dangerouslySetInnerHTML={{ __html: marked.marked(description) }}
            ></p>
          )}
        </div>
      </div>
    </div>
  );
}
