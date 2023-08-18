import Degree from "../types/Degree";
import EnumMapper from "../../../utils/EnumMapper";
import Language from "../../../type/Language";
import { ReactComponent as Clock } from "../../../assets/icons/clock-grey.svg";
import { ReactComponent as Dollar } from "../../../assets/icons/dollar-gray.svg";
import { ReactComponent as Atom } from "../../../assets/icons/atom-gray.svg";
import { ReactComponent as LanguageIcon } from "../../../assets/icons/language-gray.svg";

export interface DegreeBasicInformationProps {
  degree: Degree;
}

export default function DegreeBasicInformation(
  props: DegreeBasicInformationProps,
) {
  const { degree } = props;

  const degreeLevel = EnumMapper.mapDegreeTitleToString(degree.title);
  const languages = degree.languages.map((language: Language) => {
    return EnumMapper.mapLanguageToString(language);
  });

  return (
    <div
      className={
        "bg-hoverGray flex flex-col h-fit py-4 px-4 w-full flex-none gap-4"
      }
    >
      <h5 className={"text-2xl text-brandMainNearlyBlack"}>
        Basic information
      </h5>
      <div className={"flex items-center gap-4"}>
        <Clock className={"h-8 w-8"} />
        <div className={"flex flex-col text-grayscaleDark"}>
          <span>Length of studies</span>
          <span className={"font-bold"}>{degree.lengthOfStudy} years</span>
        </div>
      </div>
      <div className={"flex items-center gap-4"}>
        <Atom className={"h-8 w-8"} />
        <div className={"flex flex-col text-grayscaleDark"}>
          <span>Degree level</span>
          <span className={"font-bold"}>{degreeLevel}</span>
        </div>
      </div>
      <div className={"flex items-center gap-4"}>
        <LanguageIcon className={"h-8 w-8"} />
        <div className={"flex flex-col text-grayscaleDark"}>
          <span>Languages</span>
          <span className={"font-bold"}>{languages.join(", ")}</span>
        </div>
      </div>
      <div className={"flex items-center gap-4"}>
        <Dollar className={"h-8 w-8"} />
        <div className={"flex flex-col text-grayscaleDark"}>
          <span>Tuition fee per year</span>
          <span className={"font-bold"}>{degree.tuitionFeePerYear}</span>
        </div>
      </div>
    </div>
  );
}
