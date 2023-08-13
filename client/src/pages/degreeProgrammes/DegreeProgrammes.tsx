import { Degree, DegreeRequest } from "../../features/degree";
import { useEffect, useState } from "react";
import { useAppSelector } from "../../hooks";
import { GetDegreesResponse } from "../../features/degree/services/DegreeRequest";
import PaginationInfo from "../../type/PaginationInfo";
import { Sidebar } from "../../features/sidebar";
import RequestService from "../../utils/RequestService";
import { SortDirection } from "../../type/PaginationParams";
import ResourceNameWithLink from "../../type/ResourceNameWithLink";

export default function DegreeProgrammes() {
  const mobileNavView = useAppSelector(
    (state) => state.integration.mobileNavView,
  );
  const [faculties, setFaculties] = useState<ResourceNameWithLink[]>([]);
  const links = useAppSelector((state) => state.links);
  const [degrees, setDegrees] = useState<Degree[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [page, setPage] = useState<number>(0);
  const [paginationInfo, setPaginationInfo] = useState<PaginationInfo>({
    page: 0,
    size: 0,
    totalElements: 0,
    totalPages: 0,
  });

  useEffect(() => {
    const handleFetchFaculties = async () => {
      // Prepare the link
      if (!links.faculties.getFaculties) {
        return;
      }

      // Call the api
      const response = await RequestService.performGetRequest({
        link: links.faculties.getFaculties,
        pagination: {
          page: page,
          size: 100,
          sort: ["name", SortDirection.ASC],
        },
      });

      // Convert the response data into resources with links
      const facultiesArr: ResourceNameWithLink[] = [];
      response._embedded.faculties.forEach((faculty: any) => {
        const facultyNameWithLink: ResourceNameWithLink = {
          name: faculty.name,
          link: faculty._links.self.href,
          id: faculty.id,
        };
        facultiesArr.push(facultyNameWithLink);
      });

      setFaculties(facultiesArr);
    };

    handleFetchFaculties();
  }, [links]);

  useEffect(() => {
    const handleFetchDegrees = async () => {
      // Prepare the link
      if (!links.degrees.getDegrees) {
        return;
      }

      // Call the api
      const response: GetDegreesResponse = await DegreeRequest.getList({
        link: links.degrees.getDegrees,
        pagination: { page: page },
      });

      setDegrees(response.degrees);
      setPaginationInfo(response.paginationInfo);
    };

    handleFetchDegrees();
  }, [links, page]);

  const changePage = (direction: "next" | "previous") => {
    setPage((prevPage) => {
      if (direction === "next") {
        return prevPage + 1;
      } else {
        return prevPage - 1;
      }
    });
  };

  return (
    <div className={"flex h-full"}>
      <Sidebar />
      <main className={"h-full w-full flex flex-col lg:px-8 py-8"}>
        {/* Title and text */}
        <h1 className="page-title_h1 text-brandMainNearlyBlack">
          Our degree programmes
        </h1>
        <p className={"text-grayscaleDarkText mb-6"}>
          Aquila university has {paginationInfo.totalElements} degree
          programmes. Find something that fits you and study with us.
        </p>
        <form>
          <div className="flex">
            <label
              htmlFor="search-dropdown"
              className="mb-2 text-sm font-medium text-gray-900 sr-only"
            >
              Field of study
            </label>
            {/* Search by field of study */}
            <div className="relative w-full">
              <input
                type="search"
                id="search-dropdown"
                className="block p-4 w-full font-bold text-sm text-black bg-grayscaleLight  border border-grayscaleMedium focus:ring-blue-500 focus:border-blue-500"
                placeholder="Search for a field of study..."
                required
              />
              <button
                type="submit"
                className="absolute top-0 right-0 p-2.5 text-sm font-medium h-full text-white bg-blue-700 rounded-r-lg border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
              >
                <svg
                  className="w-4 h-4"
                  aria-hidden="true"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 20 20"
                >
                  <path
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"
                  />
                </svg>
                <span className="sr-only">Search</span>
              </button>
            </div>
          </div>
          <div className={"flex"}>
            {/* Faculties dropdown */}
            <select
              name="faculty"
              id="faculty"
              className={
                " w-3/5 block p-2 mb-6 text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
              }
            >
              <option value={"null"}>All faculties</option>
              {faculties.map((faculty) => (
                <option value={faculty.id.toString()}>{faculty.name}</option>
              ))}
            </select>
          </div>
        </form>
      </main>
    </div>
  );
}
