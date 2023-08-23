import UserData from "../types/UserData";
import DegreeCardPlaceholder from "../../degree/assets/degreeCardPlaceholder.jpg";
import { Link } from "react-router-dom";
import { AppPaths } from "../../../App";

export interface UserListCardProps {
  user: UserData;
}

export default function UserListCard(props: UserListCardProps) {
  const { user } = props;

  return (
    <Link
      to={`/users/${user.id.toString()}`}
      className={
        "flex flex-col w-full  bg-hoverGray border border-gray-200 rounded shadow  hover:bg-grey"
      }
    >
      <img
        className={`rounded-t-lg h-72 w-full sm:h-auto md:rounded-none md:rounded-l `}
        src={
          user.profilePictureName
            ? `${AppPaths.IMAGES}/${user.profilePictureName}`
            : require("../assets/blank-profile-picture.webp")
        }
        alt={"Profile picture"}
      />

      <div
        className={`flex flex-col w-full justify-between p-2 py-4 h-full lg:px-6 `}
      >
        <div>
          <h5 className="mb-2 text-xl  font-bold tracking-tight text-brandMain">
            {user.firstName} {user.lastName}
          </h5>
        </div>
      </div>
    </Link>
  );
}
