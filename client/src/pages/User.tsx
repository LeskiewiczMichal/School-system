import { useAppSelector } from "../hooks";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import UserRequest from "../features/user/services/UserRequest";
import {
  BasicInformation,
  TeacherDetails,
  TeacherDetailsMapper,
  UserData,
} from "../features/user";
import LoadingSpinner from "../common_components/LoadingSpinner";
import { Sidebar } from "../features/sidebar";
import ColoredBackgroundWithPhotoOnRight from "../common_components/Card/ColoredBackgroundWithPhotoOnRight";
import CommunityPicture from "./assets/community.webp";
import Role from "../type/Role";
import axios from "axios";

export default function User() {
  const links = useAppSelector((state) => state.links);
  const { userId } = useParams<{ userId: string }>();
  const [user, setUser] = useState<UserData | null>(null);
  const [teacherDetails, setTeacherDetails] = useState<TeacherDetails | null>(
    null,
  );

  useEffect(() => {
    const handleFetchUser = async () => {
      // Prepare the link
      if (!links.users.getById || !userId) {
        return;
      }

      // Call the API
      const response: UserData = await UserRequest.getById({
        link: links.users.getById,
        id: userId,
      });

      // Set the user
      setUser(response);

      if (response.role === Role.TEACHER) {
        if (!response.teacherDetails) {
          return;
        }

        const teacherDetailsResponse = await axios.get(
          response.teacherDetails.href,
        );

        const teacherDetails: TeacherDetails =
          TeacherDetailsMapper.mapFromServerData(teacherDetailsResponse.data);

        setTeacherDetails(teacherDetails);
      }
    };

    handleFetchUser();
  }, [links]);

  if (!user) {
    return <LoadingSpinner />;
  }

  return (
    <div className={"flex h-full"}>
      <Sidebar />
      <main className={"pb-16 border-b border-grayscaleMediumDark w-full"}>
        <section
          className={
            "flex flex-col px-4 lg:px-8 py-8 mb-4 w-full lg:w-1/2 lg:gap-4"
          }
        >
          {/*<ColoredBackgroundWithPhotoOnRight*/}
          {/*  heading={`Hello, ${user.firstName}`}*/}
          {/*  text={"Thank you for being a part of our community"}*/}
          {/*  imageLink={CommunityPicture}*/}
          {/*  backgroundColor={"brandMain"}*/}
          {/*/>*/}
          <BasicInformation user={user} />
        </section>
        <section
          className={
            "px-4 lg:px-8 lg:pr-28 py-8 mb-4 w-full border-t border-grayscaleMedium"
          }
        ></section>
      </main>
    </div>
  );
}
