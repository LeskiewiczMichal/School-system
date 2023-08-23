import { DegreeTitle } from "../../degree";
import { useAppSelector } from "../../../hooks";

export interface UserSearchFormProps {
  nameField: string;
  formChangeHandler: (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => void;
  handleFetchUsers: () => Promise<void>;
  setPage: React.Dispatch<React.SetStateAction<number>>;
}

export default function UserSearchForm(props: UserSearchFormProps) {
  const { nameField, formChangeHandler, handleFetchUsers, setPage } = props;
  const links = useAppSelector((state) => state.links);

  return (
    <form className={"border border-grayscaleDark px-4 py-8 mb-12"}>
      <h6 className={"font-bold text-brandMainNearlyBlack text-lg mb-8"}>
        Search for your friends!
      </h6>
      <div className="flex mb-8">
        <label
          htmlFor="search-dropdown"
          className="mb-2 text-sm font-medium text-gray-900 sr-only"
        >
          Name
        </label>
        {/* Search by name */}
        <div className="relative w-full lg:w-2/3">
          <input
            type="search"
            id="name"
            name="namme"
            className="block p-4 w-full font-bold text-sm text-black bg-grayscaleLight border-2 border-grayscaleMediumDark focus:outline-none focus:ring-none focus:border-brandMain"
            placeholder="Search for a field of study..."
            onChange={formChangeHandler}
            value={nameField}
            required
          />
          <button
            type="button"
            onClick={() => {
              setPage(0);
              handleFetchUsers();
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
    </form>
  );
}
