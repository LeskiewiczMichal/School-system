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
import Role from "../type/Role";
import axios from "axios";
import MyHeading from "../common_components/MyHeading";
import MyHeadingWithLine from "../common_components/MyHeadingWithLine";
import * as marked from "marked";
import { SidebarLinkProps } from "../features/sidebar/components/SidebarLink";

export default function User() {
  const links = useAppSelector((state) => state.links);
  const { userId } = useParams<{ userId: string }>();
  const [user, setUser] = useState<UserData | null>(null);
  const [teacherDetails, setTeacherDetails] = useState<TeacherDetails | null>(
    null,
  );

  const sidebarLinks: SidebarLinkProps[] = [
    {
      title: "Academic Staff",
      redirectUrl: "/academic-staff",
    },
    {
      title: "All Users",
      redirectUrl: "/users",
    },
  ];

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
      <Sidebar links={sidebarLinks} />
      <main className={"pb-16 w-full"}>
        {/* Basic info */}
        <section
          className={
            "flex flex-col px-4 lg:px-8 py-8 mb-4 w-full justify-center items-center lg:gap-4"
          }
        >
          <BasicInformation
            user={user}
            teacherDetails={teacherDetails ? teacherDetails : undefined}
          />
        </section>

        {/* Biography */}
        {teacherDetails && (
          <>
            <section className={"py-8 mb-4 w-full "}>
              <div className={"pl-4 lg:p-8"}>
                <MyHeadingWithLine heading={"Biography"} />
              </div>

              <div className={"px-5 lg:px-12 lg:pr-28 "}>
                <p
                  className={"text-grayscaleDarkText"}
                  dangerouslySetInnerHTML={{
                    __html: marked.marked(teacherDetails.bio),
                  }}
                ></p>
              </div>
            </section>

            <section className={"py-8 mb-4 w-full "}>
              <div className={"pl-4 lg:p-8"}>
                <MyHeadingWithLine heading={"Tutorship"} />
              </div>

              <div className={"px-5 lg:px-12 lg:pr-28 "}>
                <p
                  className={"text-grayscaleDarkText"}
                  dangerouslySetInnerHTML={{
                    __html: marked.marked(teacherDetails.tutorship),
                  }}
                ></p>
              </div>
            </section>
          </>
        )}
      </main>
    </div>
  );
}
