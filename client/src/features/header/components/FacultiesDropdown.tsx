import { useEffect, useState, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../../hooks";
import { ReactComponent as DropdownArrow } from "../../../assets/icons/dropdown-arrow.svg";
import RequestService from "../../../utils/RequestService";
import APILink from "../../../type/APILink";
import { Faculty } from "../../faculty";
import ResourceNameWithLink from "../../../type/ResourceNameWithLink";

export default function FacultiesDropdown() {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [profileMenuOpen, setProfileMenuOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);
  const facultiesLink: APILink | null = useAppSelector(
    (state) => state.links.faculties,
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
        });

        // Convert the response data into resources with links
        const facultiesArr: ResourceNameWithLink[] = [];
        responseData._embedded.faculties.forEach((faculty: any) => {
          const facultyNameWithLink: ResourceNameWithLink = {
            name: faculty.name,
            link: faculty._links.self,
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
        className="flex flex-col items-center inline-block text-md font-bold text-primary align-baseline hover:text-primaryLighter h-full"
        type="button"
        onClick={handleDropDown}
      >
        <span>Faculties</span>
        <DropdownArrow className="h-5 w-6" />{" "}
        {/* Or your DropdownArrow component */}
      </button>

      {profileMenuOpen && (
        <div className="fixed z-40 top-20 left-0 right-0 bottom-0 bg-gray-600 opacity-40" />
      )}

      {/* Dropdown */}
      <nav
        aria-labelledby="profileButton"
        className={`z-50 h-3/5 bg-white border-b rounded-b-lg w-screen left-0  top-20  ${
          profileMenuOpen ? "absolute" : "hidden"
        }`}
      >
        {/* User info */}
        <div className="px-4 py-3 text-sm text-gray-900 dark:text-white"></div>
        <ul
          className="py-2 text-sm text-gray-700 dark:text-gray-200"
          aria-labelledby="dropdownUserAvatarButton"
        >
          {/* Profile link button */}
          <li>
            {faculties.map((faculty) => (
              <a href={faculty.link}>{faculty.name}</a>
            ))}
          </li>
        </ul>
      </nav>
    </div>
  );
}
