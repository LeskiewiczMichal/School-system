import Greetings from "../../common_components/Card/Greetings";
import { ReactComponent as Clock } from "../../assets/icons/clock-grey.svg";
import { useEffect, useState } from "react";
import { Degree as DegreeType, DegreeRequest } from "../../features/degree";
import { useAppSelector } from "../../hooks";
import { useParams } from "react-router-dom";
import { AppPaths } from "../../App";
import DegreePageContentInterface from "./DegreePageContentInterface";
import * as marked from "marked";
import EnumMapper from "../../utils/EnumMapper";

export default function Degree() {
  const { degreeId } = useParams<{ degreeId: string }>();
  const [degree, setDegree] = useState<DegreeType | null>(null);
  const degreesLinks = useAppSelector((state) => state.links.degrees);

  const degreePageContent: DegreePageContentInterface = require(`./json/degree-${degreeId}.json`);

  useEffect(() => {
    const handleFetchDegree = async () => {
      // Prepare the link
      if (!degreesLinks.getById || !degreeId) {
        return;
      }

      // Call the API
      const response = await DegreeRequest.getSingle({
        link: degreesLinks.getById,
        id: degreeId,
      });

      // Set the degree
      setDegree(response);
    };

    handleFetchDegree();
  }, [degreesLinks, degreeId]);

  if (!degree) {
    return <span>Loading</span>;
  }

  const degreeLevel = EnumMapper.mapDegreeTitleToString(degree.title);

  return (
    <div>
      {/* Top greeting */}
      <Greetings
        imageLink={`${AppPaths.IMAGES}/${degree.imageName}`}
        heading={degreePageContent.greetingsHeading}
        text={degreePageContent.greetingsText}
      />
      <section className={"flex px-4 md:px-32 my-16 gap-8"}>
        <div
          className={"px-4 lg:px-0"}
          dangerouslySetInnerHTML={{
            __html: marked.marked(degree.description),
          }}
        ></div>
        <div
          className={
            "bg-hoverGray flex flex-col h-fit py-2 px-4 w-1/3 flex-none gap-4"
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
            <Clock className={"h-8 w-8"} />
            <div className={"flex flex-col text-grayscaleDark"}>
              <span>Degree level</span>
              <span className={"font-bold"}>{degreeLevel}</span>
            </div>
          </div>
          <div className={"flex items-center gap-4"}>
            <Clock className={"h-8 w-8"} />
            <div className={"flex flex-col text-grayscaleDark"}>
              <span>Languages</span>
              <span className={"font-bold"}>
                {degree.languages.map((language) => {
                  return "English";
                })}
              </span>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
