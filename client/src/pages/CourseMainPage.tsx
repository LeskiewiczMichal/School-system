import { useEffect, useState } from "react";
import JWTUtils from "../utils/JWTUtils";
import axios from "axios/index";
import { useParams } from "react-router-dom";
import {
  Course as CourseType,
  checkIfUserIsEnrolled,
  CourseRequest,
} from "../features/course";
import APILink from "../type/APILink";
import { useAppSelector } from "../hooks";
import HaveToBeLoggedInInfo from "../common_components/HaveToBeLoggedInInfo";
import { AppPaths } from "../App";
import LinkButtonBorderOnly from "../common_components/button/LinkButtonBorderOnly";
import LoadingSpinner from "../common_components/LoadingSpinner";
import LoadingSpinnerPage from "./LoadingSpinnerPage";

export default function CourseMainPage() {
  const { courseId } = useParams<{ courseId: string }>();
  const [isLoading, setIsLoading] = useState<boolean>(true);

  const user = useAppSelector((state) => state.auth.data);
  const links = useAppSelector((state) => state.links);
  const [isUserEnrolled, setIsUserEnrolled] = useState<boolean>(false);
  const [course, setCourse] = useState<CourseType | null>(null);

  useEffect(() => {
    const handleFetchCourse = async () => {
      // Prepare the link
      if (!links.courses.getById || !courseId) {
        return;
      }

      // Call the API
      const response = await CourseRequest.getById({
        link: links.courses.getById,
        id: courseId,
      });

      // Set the course
      setCourse(response);
    };

    handleFetchCourse();
  }, [courseId, links]);

  useEffect(() => {
    if (!course) {
      return;
    }

    const handleCheckIfUserIsEnrolled = async () => {
      setIsUserEnrolled(await checkIfUserIsEnrolled(course.isUserEnrolled));
      setIsLoading(false);
    };

    handleCheckIfUserIsEnrolled();
  }, [course]);

  if (isLoading) {
    return <LoadingSpinnerPage />;
  }

  if (!user) {
    return (
      <HaveToBeLoggedInInfo
        button={{
          text: "Go to login",
          link: AppPaths.LOGIN,
          color: "brandMain",
        }}
      />
    );
  }

  if (!isUserEnrolled) {
    return (
      <HaveToBeLoggedInInfo text={"You are not enrolled in this course"} />
    );
  }

  return (
    <div>
      <h1>CourseMainPage</h1>
    </div>
  );
}
