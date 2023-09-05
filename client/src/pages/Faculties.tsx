import ColoredBackgroundWithPhotoOnRight from "../common_components/Card/ColoredBackgroundWithPhotoOnRight";
import FacultiesPagePhoto from "./assets/faculties-page.webp";
import { useEffect, useState } from "react";
import RequestService from "../utils/RequestService";
import { SortDirection } from "../type/PaginationParams";
import ResourceNameWithLink from "../type/ResourceNameWithLink";
import { useAppSelector } from "../hooks";
import LinkButtonBorderOnly from "../common_components/button/LinkButtonBorderOnly";
import { Sidebar } from "../features/sidebar";

export default function Faculties() {
  const mobileNavBar = useAppSelector(
    (state) => state.integration.mobileNavView,
  );
  const facultiesLink = useAppSelector(
    (state) => state.links.faculties.getFaculties,
  );
  const [faculties, setFaculties] = useState<ResourceNameWithLink[]>([]);

  useEffect(() => {
    const handleFetchFacultyNames = async () => {
      if (facultiesLink) {
        // Call the API
        const responseData = await RequestService.performGetRequest({
          link: facultiesLink,
          pagination: {
            page: 0,
            size: 150,
            sort: ["name", SortDirection.ASC],
          },
        });

        // Convert the response data into resources with links
        const facultiesArr: ResourceNameWithLink[] = [];
        responseData._embedded.faculties.forEach((faculty: any) => {
          const facultyNameWithLink: ResourceNameWithLink = {
            name: faculty.name,
            link: faculty._links.self.href,
            id: faculty.id,
          };
          facultiesArr.push(facultyNameWithLink);
        });

        // Set the state
        setFaculties(facultiesArr);
      }
    };

    handleFetchFacultyNames();
  }, [facultiesLink]);

  return (
    <main className={"lg:p-24"}>
      {mobileNavBar && <Sidebar />}
      <div className={""}>
        <ColoredBackgroundWithPhotoOnRight
          heading={"Explore Our Diverse Faculties at Aquila University"}
          text={
            "Welcome to Aquila University's list of faculties, where academic excellence thrives and limitless possibilities await. Discover your passion, uncover new horizons, and embark on a journey of personal and intellectual growth."
          }
          imageLink={FacultiesPagePhoto}
        />
      </div>

      <section className={"px-6 lg:px-0"}>
        <h4 className={"my-header mt-10 mb-6"}>FACULTIES LIST:</h4>

        <ul
          className={
            "flex flex-col gap-8 w-full px-2 sm:px-6 lg:px-0 justify-center  md:grid md:grid-cols-2 mb-6"
          }
        >
          {faculties.map((faculty) => (
            <li key={faculty.id.toString()}>
              <LinkButtonBorderOnly
                text={faculty.name}
                link={`/faculties/${faculty.id}`}
                color={"black"}
                width={"w-full"}
              />
            </li>
          ))}
        </ul>
      </section>
    </main>
  );
}
