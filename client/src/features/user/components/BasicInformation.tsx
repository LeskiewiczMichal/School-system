import UserData from "../UserData";
import EnumMapper from "../../../utils/EnumMapper";
import { ReactComponent as DegreeDiploma } from "../assets/degree-diploma.svg";
import { ReactComponent as Email } from "../assets/email.svg";
import { ReactComponent as IdentificationCard } from "../assets/identification-card.svg";
import { ReactComponent as SchoolCampus } from "../assets/school-campus.svg";
import { ReactComponent as User } from "../assets/user.svg";

export interface BasicInformationProps {
  user: UserData;
}

export default function BasicInformation(props: BasicInformationProps) {
  const { user } = props;

  return (
    <div
      className={
        "bg-hoverGray flex flex-col h-fit py-4 px-4 w-full flex-none gap-4 "
      }
    >
      <h5 className={"text-2xl text-grayscaleDark font-bold"}>
        Basic information
      </h5>
      <div className={"flex items-center gap-4"}>
        <User className={"h-8 w-8"} />
        <div className={"flex flex-col text-grayscaleDark"}>
          <span>Name</span>
          <span className={"font-bold"}>
            {user.firstName} {user.lastName}
          </span>
        </div>
      </div>
      <div className={"flex items-center gap-4"}>
        <Email className={"h-8 w-8"} />
        <div className={"flex flex-col text-grayscaleDark"}>
          <span>Email</span>
          <span className={"font-bold"}>{user.email}</span>
        </div>
      </div>
      <div className={"flex items-center gap-4"}>
        <IdentificationCard className={"h-8 w-8"} />
        <div className={"flex flex-col text-grayscaleDark"}>
          <span>Role</span>
          <span className={"font-bold"}>
            {EnumMapper.mapRoleToString(user.role)}
          </span>
        </div>
      </div>
      <div className={"flex items-center gap-4"}>
        <SchoolCampus className={"h-8 w-8"} />
        <div className={"flex flex-col text-grayscaleDark"}>
          <span>Faculty</span>
          <span className={"font-bold"}>{user.faculty}</span>
        </div>
      </div>
      <div className={"flex items-center gap-4"}>
        <DegreeDiploma className={"h-8 w-8"} />
        <div className={"flex flex-col text-grayscaleDark"}>
          <span>Degree</span>
          <span className={"font-bold"}>{user.degree?.name}</span>
        </div>
      </div>
    </div>
  );
}
