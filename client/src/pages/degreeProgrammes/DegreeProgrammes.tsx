import {
  Degree,
  DegreeCard,
  DegreeRequest,
  DegreeSearchForm,
  DegreeTitle,
} from "../../features/degree";
import { useEffect, useState } from "react";
import { useAppSelector } from "../../hooks";
import { GetDegreesResponse } from "../../features/degree/services/DegreeRequest";
import PaginationInfo from "../../type/PaginationInfo";
import { Sidebar } from "../../features/sidebar";
import RequestService from "../../utils/RequestService";
import { SortDirection } from "../../type/PaginationParams";
import ResourceNameWithLink from "../../type/ResourceNameWithLink";
import MyHeading from "../../common_components/MyHeading";
import { SidebarLinkProps } from "../../features/sidebar/components/SidebarLink";
import { AppPaths } from "../../App";
import LoadingSpinner from "../../common_components/LoadingSpinner";
import { ReactComponent as ChevronLeft } from "../../assets/icons/chevron/chevron-left.svg";
import { ReactComponent as ChevronRight } from "../../assets/icons/chevron/chevron-right.svg";
import PaginationButtons from "../../common_components/PaginationButtons";

export default function DegreeProgrammes() {
  const mobileNavView = useAppSelector(
    (state) => state.integration.mobileNavView,
  );
  const links = useAppSelector((state) => state.links);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  // Searching
  const [page, setPage] = useState<number>(0);
  const [fieldOfStudy, setFieldOfStudy] = useState<string>("");
  const [faculty, setFaculty] = useState<string>("");
  const [title, setTitle] = useState<DegreeTitle | string>("");

  // Degree content
  const [degrees, setDegrees] = useState<Degree[]>([]);
  const [paginationInfo, setPaginationInfo] = useState<PaginationInfo>({
    page: 0,
    size: 0,
    totalElements: 0,
    totalPages: 0,
  });

  const handleFetchDegrees = async () => {
    // Prepare the link
    if (!links.degrees.getDegrees || !links.degrees.search) {
      return;
    }

    let apiLink = links.degrees.getDegrees;
    if (faculty !== "" || fieldOfStudy !== "" || title !== "") {
      apiLink = links.degrees.search;
    }

    // Call the api
    const response: GetDegreesResponse = await DegreeRequest.getList({
      link: apiLink,
      pagination: { page: page },
      faculty: faculty,
      fieldOfStudy: fieldOfStudy,
      title: title,
    });

    setDegrees(response.degrees);
    setPaginationInfo(response.paginationInfo);
    setIsLoading(false);
  };

  useEffect(() => {
    handleFetchDegrees();
  }, [links, page, faculty, title]);

  const changePage = (direction: "next" | "previous") => {
    setPage((prevPage) => {
      if (direction === "next") {
        return prevPage + 1;
      } else {
        return prevPage - 1;
      }
    });
  };

  const formChangeHandler = (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    if (event.target.name === "fieldOfStudy") {
      setFieldOfStudy(event.target.value);
    }
    if (event.target.name === "faculty") {
      setFaculty(event.target.value);
      setPage(0);
    }
    if (event.target.name === "title") {
      setTitle(event.target.value as DegreeTitle);
      setPage(0);
    }
  };

  const sidebarLinks: SidebarLinkProps[] = [
    {
      title: "Search for degree programmes",
      redirectUrl: AppPaths.DEGREE_PROGRAMMES,
      active: true,
    },
    {
      title: "Search for courses",
      redirectUrl: AppPaths.COURSES,
      active: false,
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

        <DegreeSearchForm
          fieldOfStudyField={fieldOfStudy}
          formChangeHandler={formChangeHandler}
          handleFetchDegrees={handleFetchDegrees}
          setPage={setPage}
        />

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
