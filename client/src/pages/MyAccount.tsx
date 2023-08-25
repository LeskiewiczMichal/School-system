import { Sidebar } from "../features/sidebar";
import { useAppSelector } from "../hooks";
import EnumMapper from "../utils/EnumMapper";
import { BasicInformation } from "../features/user";
import ColoredBackgroundWithPhotoOnRight from "../common_components/Card/ColoredBackgroundWithPhotoOnRight";
import CommunityPicture from "./assets/community.webp";
import LinkButtonBorderOnly from "../common_components/button/LinkButtonBorderOnly";
import { useEffect } from "react";
import { FetchCoursesResponse } from "../features/course/services/CourseRequest";
import { CourseRequest } from "../features/course";

export default function MyAccount() {
  const user = useAppSelector((state) => state.auth.data);
  const userLinks = useAppSelector((state) => state.auth._links);
  const mobileNavBar = useAppSelector(
    (state) => state.integration.mobileNavView,
  );

  if (!user) {
    return (
      <div className={"flex"}>
        <h1>Not logged in</h1>
      </div>
    );
  }

  useEffect(() => {
    const handleFetchCourses = async () => {
      // Prepare the link
      if (!degree.courses.link) {
        return;
      }
      let apiLink = degree.courses.link;

      // Call the api
      const response: FetchCoursesResponse = await CourseRequest.getList({
        link: apiLink,
        pagination: { size: 50 },
      });

      setCourses(response.courses);
      setPaginationInfo(response.paginationInfo);
    };

    handleFetchCourses();
  }, [degree]);

  return (
    <div className={"flex h-full"}>
      {mobileNavBar && <Sidebar />}
      <main
        className={
          "pb-16 border-b sm:px-16 lg:px-0 border-grayscaleMediumDark w-full"
        }
      >
        <section
          className={"flex flex-col px-4 lg:px-32 py-8 mb-4 w-full lg:gap-4"}
        >
          <BasicInformation user={user} />
        </section>
        {/*<section className={"px-6 lg:px-0"}>*/}
        {/*    <h4 className={"my-header mt-10 mb-6"}>FACULTIES LIST:</h4>*/}

        {/*    <ul*/}
        {/*        className={*/}
        {/*            "flex flex-col gap-8 w-full px-2 sm:px-6 lg:px-0 justify-center  md:grid md:grid-cols-2 mb-6"*/}
        {/*        }*/}
        {/*    >*/}
        {/*        {faculties.map((faculty) => (*/}
        {/*            <li key={faculty.id.toString()}>*/}
        {/*                <LinkButtonBorderOnly*/}
        {/*                    text={faculty.name}*/}
        {/*                    link={`/faculties/${faculty.id}`}*/}
        {/*                    color={"black"}*/}
        {/*                    width={"w-full"}*/}
        {/*                />*/}
        {/*            </li>*/}
        {/*        ))}*/}
        {/*    </ul>*/}
        {/*</section>*/}
      </main>
    </div>
  );
}
