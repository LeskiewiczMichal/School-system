import { Degree, DegreeRequest, DegreeTitle } from "../../features/degree";
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
  const links = useAppSelector((state) => state.links);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  // Searching
  const [faculties, setFaculties] = useState<ResourceNameWithLink[]>([]);
  const [page, setPage] = useState<number>(0);
  const [fieldOfStudy, setFieldOfStudy] = useState<string>("");
  const [faculty, setFaculty] = useState<string | undefined>(undefined);
  const [title, setTitle] = useState<DegreeTitle | undefined>(undefined);

  // Degree content
  const [degrees, setDegrees] = useState<Degree[]>([]);
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

  const formChangeHandler = (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    if (event.target.name === "fieldOfStudy") {
      setFieldOfStudy(event.target.value);
    }
    if (event.target.name === "faculty") {
      setFaculty(event.target.value);
    }
    if (event.target.name === "title") {
      setTitle(event.target.value as DegreeTitle);
    }
  };

  return (
    <div className={"flex h-full"}>
      <Sidebar />
      <main className={"h-full w-full flex flex-col lg:px-8 py-8"}>
        {/* Title and text */}
        <h1 className="page-title_h1 px-4 text-brandMainNearlyBlack lg:px-0">
          Our degree programmes
        </h1>
        <p className={"text-grayscaleDarkText px-4 mb-6 lg:px-0"}>
          Aquila university has {paginationInfo.totalElements} degree
          programmes. Find something that fits you and study with us.
        </p>
        <form className={"border border-grayscaleDark px-4 py-8"}>
          <h6 className={"font-bold text-brandMainNearlyBlack text-lg mb-8"}>
            Search in our programmes
          </h6>
          <div className="flex mb-8">
            <label
              htmlFor="search-dropdown"
              className="mb-2 text-sm font-medium text-gray-900 sr-only"
            >
              Field of study
            </label>
            {/* Search by field of study */}
            <div className="relative w-full lg:w-2/3">
              <input
                type="search"
                id="fieldOfStudy"
                name="fieldOfStudy"
                className="block p-4 w-full font-bold text-sm text-black bg-grayscaleLight border-2 border-grayscaleMediumDark focus:outline-none focus:ring-none focus:border-brandMain"
                placeholder="Search for a field of study..."
                onChange={formChangeHandler}
                value={fieldOfStudy}
                required
              />
              <button
                type="submit"
                className="absolute top-0 right-0 p-2.5 text-sm font-medium h-full text-white bg-brandMain px-4 hover:bg-brandMainActive focus:ring-4 focus:outline-none focus:ring-blue-300"
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
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"
                  />
                </svg>
                <span className="sr-only">Search</span>
              </button>
            </div>
          </div>
          <div className={"flex gap-8"}>
            {/* Faculties dropdown */}
            <select
              name="faculty"
              id="faculty"
              className={
                "w-full lg:w-1/5 p-2 mb-6  text-brandMain font-bold border border-grayscaleMediumDark focus:ring-bradMain focus:outline-none focus:border-brandMain"
              }
              onChange={formChangeHandler}
              defaultValue={undefined}
            >
              <option value={undefined}>All faculties</option>
              {faculties.map((faculty) => (
                <option
                  key={faculty.id.toString()}
                  value={faculty.id.toString()}
                >
                  {faculty.name}
                </option>
              ))}
            </select>
            {/* Degree titles dropdown */}
            <select
              name="title"
              id="title"
              className={
                "w-full lg:w-1/5 p-2 mb-6  text-brandMain font-bold border border-grayscaleMediumDark focus:ring-bradMain focus:outline-none focus:border-brandMain"
              }
              onChange={formChangeHandler}
              defaultValue={undefined}
            >
              <option value={undefined}>Degree level</option>
              <option value={DegreeTitle.BACHELOR}>Bachelor</option>
              <option value={DegreeTitle.BACHELOR_OF_SCIENCE}>
                Bachelor of science
              </option>
              <option value={DegreeTitle.MASTER}>Master</option>
              <option value={DegreeTitle.DOCTOR}>Doctoral</option>
              <option value={DegreeTitle.PROFESSOR}>Professor</option>
            </select>
          </div>
        </form>
      </main>
    </div>
  );
}
