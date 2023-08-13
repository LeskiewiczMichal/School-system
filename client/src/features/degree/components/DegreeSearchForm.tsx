import { DegreeTitle } from "../index";
import { useEffect, useState } from "react";
import RequestService from "../../../utils/RequestService";
import { SortDirection } from "../../../type/PaginationParams";
import ResourceNameWithLink from "../../../type/ResourceNameWithLink";
import { useAppSelector } from "../../../hooks";

export interface DegreeSearchFormProps {
  fieldOfStudyField: string;
  formChangeHandler: (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => void;
  handleFetchDegrees: () => Promise<void>;
  setPage: React.Dispatch<React.SetStateAction<number>>;
}

export default function DegreeSearchForm(props: DegreeSearchFormProps) {
  const { formChangeHandler, fieldOfStudyField, handleFetchDegrees, setPage } =
    props;
  const links = useAppSelector((state) => state.links);
  const [faculties, setFaculties] = useState<ResourceNameWithLink[]>([]);

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
          page: 0,
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

  return (
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
            value={fieldOfStudyField}
            required
          />
          <button
            type="button"
            onClick={() => {
              setPage(0);
              handleFetchDegrees();
            }}
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
            <option key={faculty.id.toString()} value={faculty.id.toString()}>
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
  );
}
