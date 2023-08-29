import { Link } from "react-router-dom";
import Degree from "../types/Degree";
import DegreeCardPlaceholder from "../assets/degreeCardPlaceholder.jpg";
import EnumMapper from "../../../utils/EnumMapper";
import { AppPaths } from "../../../App";

export interface DegreeCardProps {
  degree: Degree;
}

export default function DegreeCard(props: DegreeCardProps) {
  const { degree } = props;

  const truncatedText =
    degree.description.length > 150
      ? degree.description.slice(0, 150) + "..."
      : degree.description;

  let degreeLevel = EnumMapper.mapDegreeTitleToString(degree.title);

  return (
    <Link
      to={`/degree-programmes/${degree.id.toString()}`}
      className={
        "flex w-full flex-col sm:flex-row bg-hoverGray border border-gray-200 rounded shadow  hover:bg-grey "
      }
    >
      <img
        className={`rounded-t-lg h-72 w-full sm:h-auto sm:w-1/4 md:rounded-none md:rounded-l `}
        src={
          degree.imageName
            ? `${AppPaths.IMAGES}/${degree.imageName}`
            : DegreeCardPlaceholder
        }
        alt={"Degree image"}
      />

      <div
        className={`flex flex-col w-full justify-between p-2 py-4 h-full lg:px-6 `}
      >
        <div>
          <h5 className="mb-2 text-xl  font-bold tracking-tight text-brandMain">
            {degree.fieldOfStudy} | {degreeLevel}'s programme
          </h5>
          <p className="mb-3 font-normal text-grayscaleDarkText ">
            {truncatedText}
          </p>
        </div>
      </div>
    </Link>
  );
}
