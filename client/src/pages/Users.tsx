import { useAppSelector } from "../hooks";
import { Sidebar } from "../features/sidebar";
import { SidebarLinkProps } from "../features/sidebar/components/SidebarLink";
import { DegreeCard, DegreeSearchForm } from "../features/degree";
import MyHeading from "../common_components/MyHeading";
import LoadingSpinner from "../common_components/LoadingSpinner";
import PaginationButtons from "../common_components/PaginationButtons";
import { useState } from "react";
import { UserData } from "../features/user";
import PaginationInfo from "../type/PaginationInfo";

export default function Users() {
  const user = useAppSelector((state) => state.auth.data);
  const links = useAppSelector((state) => state.links);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  // Searching
  const [page, setPage] = useState<number>(0);
  const [name, setName] = useState<string>("");

  // Users content
  const [users, setUsers] = useState<UserData[]>([]);
  const [paginationInfo, setPaginationInfo] = useState<PaginationInfo>({
    page: 0,
    size: 0,
    totalElements: 0,
    totalPages: 0,
  });

  const sidebarLinks: SidebarLinkProps[] = [
    {
      title: "Academic Staff",
      redirectUrl: "/academic-staff",
    },
    {
      title: "All Users",
      redirectUrl: "/users",
      active: true,
    },
  ];

  return (
    <div className={"flex h-full"}>
      <Sidebar links={sidebarLinks} />
      <main className={"h-full w-full flex flex-col lg:px-8 py-8"}>
        {/* Title and text */}
        <h1 className="page-title_h1 px-4 text-brandMainNearlyBlack lg:px-0">
          Our degree programmes
        </h1>
        <p className={"text-grayscaleDarkText px-4 mb-6 lg:px-0"}>
          Aquila University offers a variety of degree programs. Find the
          perfect fit for you and embark on your educational journey with us.
        </p>

        {/*<DegreeSearchForm*/}
        {/*    fieldOfStudyField={fieldOfStudy}*/}
        {/*    formChangeHandler={formChangeHandler}*/}
        {/*    handleFetchDegrees={handleFetchDegrees}*/}
        {/*    setPage={setPage}*/}
        {/*/>*/}

        {/* Degree programmes */}
        <MyHeading
          heading={`Search results (${paginationInfo.totalElements})`}
        />
        {isLoading && <LoadingSpinner />}
        {degrees.length !== 0 && !isLoading && (
          <section className={"flex flex-col gap-4 px-2 sm:px-6 lg:px-0"}>
            {degrees.map((degree) => (
              <DegreeCard key={degree.id.toString()} degree={degree} />
            ))}
          </section>
        )}
        {degrees.length === 0 && !isLoading && (
          <span>No degrees matching your requirements were found.</span>
        )}

        {/* Pagination buttons */}
        <PaginationButtons
          paginationInfo={paginationInfo}
          page={page}
          changePage={changePage}
        />
      </main>
    </div>
  );
}
