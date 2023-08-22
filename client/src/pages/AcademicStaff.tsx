import { Sidebar } from "../features/sidebar";
import { SidebarLinkProps } from "../features/sidebar/components/SidebarLink";
import { useEffect, useState } from "react";
import { useAppSelector } from "../hooks";
import UserRequest, {
  FetchUsersResponse,
} from "../features/user/services/UserRequest";
import Role from "../type/Role";
import { UserData } from "../features/user";
import PaginationInfo from "../type/PaginationInfo";
import { Link } from "react-router-dom";
import LinkButtonBorderOnly from "../common_components/button/LinkButtonBorderOnly";

export default function AcademicStaff() {
  const links = useAppSelector((state) => state.links);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [users, setUsers] = useState<UserData[]>([]);
  const [paginationInfo, setPaginationInfo] = useState<PaginationInfo>({
    page: 0,
    size: 0,
    totalElements: 0,
    totalPages: 0,
  });

  const sidebarLinks: SidebarLinkProps[] = [
    {
      title: "Teaching",
      redirectUrl: "/teaching",
      active: false,
    },
    {
      title: "Academic Staff",
      redirectUrl: "/academic-staff",
      active: true,
    },
    {
      title: "Research",
      redirectUrl: "/research",
      active: false,
    },
  ];

  useEffect(() => {
    const handleFetchTeachers = async () => {
      // Prepare the link
      if (!links.users.search) {
        return;
      }

      let apiLink = links.users.search;

      // Call the api
      const response: FetchUsersResponse = await UserRequest.getList({
        link: apiLink,
        pagination: { size: 150 },
        role: Role.TEACHER,
      });

      setUsers(response.users);
      setPaginationInfo(response.paginationInfo);
      setIsLoading(false);
    };

    handleFetchTeachers();
  }, [links]);

  return (
    <div className={"flex h-full"}>
      {/* Sidebar */}
      <Sidebar links={sidebarLinks} />
      <main className={"h-full w-full flex flex-col py-8"}>
        {/* Title and text */}
        <section className={"px-4 lg:px-8 mb-6"}>
          <h1 className="page-title_h1 text-brandMainNearlyBlack">
            Uncover the Faces Behind Aquila University's Excellence!
          </h1>

          <p className={"text-grayscaleDarkText lg:pr-20"}>
            Ever wondered who drives the dynamic learning at Aquila? Our
            exceptional teaching team is the heartbeat of our educational
            prowess. Get a glimpse into the lives of educators who are invested
            in your success. Explore their expertise, passion, and dedication to
            shaping tomorrow's leaders. Embark on a journey to discover the
            educators who will inspire your growth and fuel your ambitions.
            Start exploring now!
          </p>
        </section>
        <section
          className={
            "px-6 lg:px-20 border-t-2 border-b-2 border-brandMain py-10 w-full self-center mt-10"
          }
        >
          <h4 className={"my-header text-brandMain mb-6"}>OUR TEACHERS:</h4>

          <ul
            className={
              "flex flex-col gap-8 w-full px-2 sm:px-6 lg:px-0 justify-center  md:grid md:grid-cols-2"
            }
          >
            {users.map((teacher) => (
              <li key={teacher.id.toString()}>
                <LinkButtonBorderOnly
                  text={`${teacher.lastName} ${teacher.firstName} - ${teacher.faculty}`}
                  link={`/users/${teacher.id}`}
                  color={"brandMain"}
                  width={"w-full"}
                />
              </li>
            ))}
          </ul>
        </section>
      </main>
    </div>
  );
}
