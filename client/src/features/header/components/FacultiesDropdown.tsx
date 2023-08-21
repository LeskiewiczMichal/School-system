import { useEffect, useRef, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../../hooks";
import { ReactComponent as DropdownArrow } from "../../../assets/icons/dropdownArrow/dropdown-arrow.svg";
import { ReactComponent as DropdownArrowBrandMain } from "../../../assets/icons/dropdownArrow/dropdown-arrow-brandMain.svg";
import { ReactComponent as ArrowUpRight } from "../../../assets/icons/arrow/arrow-up-right-brandMain.svg";
import RequestService from "../../../utils/RequestService";
import APILink from "../../../type/APILink";
import ResourceNameWithLink from "../../../type/ResourceNameWithLink";
import { SortDirection } from "../../../type/PaginationParams";
import DarkenLayoutBelowIndexZ from "../../../common_components/DarkenLayoutBelowIndexZ";
import { AppPaths } from "../../../App";

export interface FacultiesDropdownProps {
  facultyHeader?: boolean;
  brandMain?: boolean;
}

export default function FacultiesDropdown(props: FacultiesDropdownProps) {
  const { facultyHeader, brandMain = false } = props;

  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [profileMenuOpen, setProfileMenuOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);
  const facultiesLink: APILink | null = useAppSelector(
    (state) => state.links.faculties.getFaculties,
  );
  const [faculties, setFaculties] = useState<ResourceNameWithLink[]>([]);

  const handleDropDown = () => {
    setProfileMenuOpen((oldState) => !oldState);
  };

  useEffect(() => {
    // Clicking outside of popup will close it
    const handleClickOutside = (event: MouseEvent) => {
      if (
        !profileMenuOpen &&
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target as Node)
      ) {
        setProfileMenuOpen(false);
      }
    };

    // Attach event
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  useEffect(() => {
    const handleFetchFacultyNames = async () => {
      if (facultiesLink) {
        // Call the API
        const responseData = await RequestService.performGetRequest({
          link: facultiesLink,
          pagination: {
            page: 0,
            size: 20,
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
    <div ref={dropdownRef}>
      {/* User picture button */}
      <button
        id="profileButton"
        className={`flex items-center inline-block text-md font-bold align-baseline h-full ${
          brandMain
            ? "text-brandMain hover:text-brandMainActive"
            : "text-white hover:underline"
        }`}
        type="button"
        onClick={handleDropDown}
      >
        <span>Faculties</span>
        {brandMain ? (
          <DropdownArrowBrandMain className="h-5 w-6" />
        ) : (
          <DropdownArrow className="h-5 w-6" />
        )}{" "}
      </button>

      {profileMenuOpen && (
        <div
          className={`fixed z-40 top-32 left-0 right-0 bottom-0 bg-gray-600 opacity-40`}
        />
      )}

      {/* Dropdown */}
      <nav
        aria-labelledby="profileButton"
        className={`z-50 flex flex-col items-center bg-white border-b rounded-b-lg w-screen left-0 ${
          facultyHeader ? "top-32" : "top-24"
        }  px-16 py-16  ${profileMenuOpen ? "absolute" : "hidden"}`}
      >
        {/* Faculties */}
        <Link
          to="/faculties"
          className="mb-0 lg:mb-4 text-2xl text-brandMain hover:text-brandMainActive font-bold italic underline"
        >
          Faculties and units <ArrowUpRight className={"inline w-8 h-8"} />
        </Link>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 w-full h-full place-content-center justify-center items-center text-xl text-brandMain mb-4">
          {faculties.map((faculty) => (
            <div className="w-full flex justify-center" key={faculty.name}>
              <a
                href={AppPaths.FACULTIES + "/" + faculty.id}
                className="flex w-fit italic justify-center items-center text-xl text-brandMain hover:text-brandMainActive hover:underline"
              >
                {faculty.name}
              </a>
            </div>
          ))}
        </div>
        {/*<ul*/}
        {/*  className="text-sm text-primary"*/}
        {/*  aria-labelledby="dropdownUserAvatarButton"*/}
        {/*>*/}
        {/*  /!* Profile link button *!/*/}
        {/*  <li>*/}
        {/*    {faculties.map((faculty) => (*/}
        {/*      <a href={faculty.link}>{faculty.name}</a>*/}
        {/*    ))}*/}
        {/*  </li>*/}
        {/*</ul>*/}
      </nav>
    </div>
  );
}
