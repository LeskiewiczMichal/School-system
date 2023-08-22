import UserData from "../types/UserData";
import EnumMapper from "../../../utils/EnumMapper";
import { ReactComponent as DegreeDiploma } from "../assets/degree-diploma.svg";
import { ReactComponent as Email } from "../assets/email.svg";
import { ReactComponent as IdentificationCard } from "../assets/identification-card.svg";
import { ReactComponent as SchoolCampus } from "../assets/school-campus.svg";
import { ReactComponent as User } from "../assets/user.svg";
import { ReactComponent as FieldOfTeacher } from "../assets/fieldOfTeacher.svg";
import TeacherDetails from "../types/TeacherDetails";

export interface BasicInformationProps {
  user: UserData;
  teacherDetails?: TeacherDetails;
}

export default function BasicInformation(props: BasicInformationProps) {
  const { user, teacherDetails } = props;

  return (
    <div
      className={
        "bg-brandMain flex flex-col h-fit py-4 px-4 w-full flex-none gap-4 lg:w-2/3"
      }
    >
      <h5 className={"text-2xl text-white font-bold"}>Basic information</h5>

      <div className={"flex items-center gap-4"}>
        <User className={"h-8 w-8"} />
        <div className={"flex flex-col text-white"}>
          <span>Name</span>
          <span className={"font-bold"}>
            {user.firstName} {user.lastName}
          </span>
        </div>
      </div>

      <div className={"flex items-center gap-4"}>
        <Email className={"h-8 w-8"} />
        <div className={"flex flex-col text-white"}>
          <span>Email</span>
          <span className={"font-bold"}>{user.email}</span>
        </div>
      </div>

      <div className={"flex items-center gap-4"}>
        <IdentificationCard className={"h-8 w-8"} />
        <div className={"flex flex-col text-white"}>
          <span>Role</span>
          <span className={"font-bold"}>
            {EnumMapper.mapRoleToString(user.role)}
          </span>
        </div>
      </div>

      <div className={"flex items-center gap-4"}>
        <SchoolCampus className={"h-8 w-8"} />
        <div className={"flex flex-col text-white"}>
          <span>Faculty</span>
          <span className={"font-bold"}>{user.faculty}</span>
        </div>
      </div>

      {user.degree && (
        <div className={"flex items-center gap-4"}>
          <DegreeDiploma className={"h-8 w-8"} />
          <div className={"flex flex-col text-white"}>
            <span>Degree</span>
            <span className={"font-bold"}>{user.degree?.name}</span>
          </div>
        </div>
      )}

      {/* If user is a teacher */}
      {teacherDetails && (
        <>
          <div className={"flex items-center gap-4"}>
            <DegreeDiploma className={"h-8 w-8"} />
            <div className={"flex flex-col text-white"}>
              <span>Academic title</span>
              <span className={"font-bold"}>
                {EnumMapper.mapDegreeTitleToString(teacherDetails.title)}
              </span>
            </div>
          </div>

          <div className={"flex items-center gap-4"}>
            <FieldOfTeacher className={"h-8 w-8"} />
            <div className={"flex flex-col text-white"}>
              <span>Field</span>
              <span className={"font-bold"}>{teacherDetails.degreeField}</span>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
