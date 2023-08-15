import { Link } from "react-router-dom";
import Degree from "../types/Degree";
import DegreeCardPlaceholder from "../assets/degreeCardPlaceholder.jpg";

export interface DegreeCardProps {
  degree: Degree;
}

export default function DegreeCard(props: DegreeCardProps) {
  const { degree } = props;

  const truncatedText =
    degree.description.length > 150
      ? degree.description.slice(0, 150) + "..."
      : degree.description;

  let degreeLevel = "";
  switch (degree.title) {
    case "BACHELOR":
      degreeLevel = "Bachelor's";
      break;
    case "BACHELOR_OF_SCIENCE":
      degreeLevel = "Bachelor of Science";
      break;
    case "MASTER":
      degreeLevel = "Master's";
      break;
    case "DOCTOR":
      degreeLevel = "Doctor's";
      break;
    case "PROFESSOR":
      degreeLevel = "Professor's";
      break;
    default:
      degreeLevel = "Bachelor's";
      break;
  }

  return (
    <Link
      to={`/degree-programmes/${degree.id.toString()}`}
      className={
        "flex w-full bg-hoverGray border border-gray-200 rounded shadow  hover:bg-grey "
      }
    >
      <img
        className={`rounded-t-lg h-96 lg:h-auto md:rounded-none md:rounded-l w-1/4 `}
        src={degree.imageName || DegreeCardPlaceholder}
        alt={"Degree image"}
      />

      <div
        className={`flex flex-col w-full justify-between p-2 py-4 h-full lg:px-6 `}
      >
        <div>
          <h5 className="mb-2 text-xl  font-bold tracking-tight text-brandMain">
            {degree.fieldOfStudy} | {degreeLevel} programme
          </h5>
          <p className="mb-3 font-normal text-grayscaleDarkText ">
            {truncatedText}
          </p>
        </div>
      </div>
    </Link>
  );
}
