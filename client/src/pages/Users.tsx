import { useAppSelector } from "../hooks";
import { Sidebar } from "../features/sidebar";
import { SidebarLinkProps } from "../features/sidebar/components/SidebarLink";
import { DegreeCard, DegreeSearchForm } from "../features/degree";
import MyHeading from "../common_components/MyHeading";
import LoadingSpinner from "../common_components/LoadingSpinner";
import PaginationButtons from "../common_components/PaginationButtons";
import { useEffect, useState } from "react";
import { UserData, UserSearchForm } from "../features/user";
import PaginationInfo from "../type/PaginationInfo";
import UserRequest, {
  FetchUsersResponse,
} from "../features/user/services/UserRequest";

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

  const changePage = (direction: "next" | "previous") => {
    setPage((prevPage) => {
      if (direction === "next") {
        return prevPage + 1;
      } else {
        return prevPage - 1;
      }
    });
  };

  const handleFetchUsers = async () => {
    // Prepare the link
    if (!links.users.getUsers || !links.users.search) {
      return;
    }

    let apiLink = links.users.getUsers;
    if (name !== "") {
      apiLink = links.users.search;
    }

    // Split name into firstName and lastName
    const nameParts = name.split(" ");
    const firstName = nameParts[0];
    const lastName = nameParts[1] || "";

    // Call the api
    const response: FetchUsersResponse = await UserRequest.getList({
      link: apiLink,
      pagination: { page: page },
      firstName: firstName,
      lastName: lastName,
    });

    setUsers(response.users);
    setPaginationInfo(response.paginationInfo);
    setIsLoading(false);
  };

  useEffect(() => {
    handleFetchUsers();
  }, [links, page]);

  const formChangeHandler = (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    if (event.target.name === "name") {
      setName(event.target.value);
    }
  };

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

        <UserSearchForm
          nameField={name}
          formChangeHandler={formChangeHandler}
          handleFetchUsers={handleFetchUsers}
          setPage={setPage}
        />

        {/* Users display */}
        <MyHeading
          heading={`Search results (${paginationInfo.totalElements})`}
        />
        {isLoading && <LoadingSpinner />}
        {users.length !== 0 && !isLoading && (
          <section className={"flex flex-col gap-4 px-2 sm:px-6 lg:px-0"}>
            {users.map((user) => (
              // <DegreeCard key={degree.id.toString()} degree={degree} />
              <span>{user.lastName}</span>
            ))}
          </section>
        )}
        {users.length === 0 && !isLoading && (
          <span>No users matching your search parameters found.</span>
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
