import Greetings from "../../common_components/Card/Greetings";
import { useEffect, useState } from "react";
import { Degree as DegreeType, DegreeRequest } from "../../features/degree";
import { useAppSelector } from "../../hooks";
import { useParams } from "react-router-dom";
import { AppPaths } from "../../App";
import FacultyPageContentInterface from "../faculty/FacultyPageContentInterface";
import DegreePageContentInterface from "./DegreePageContentInterface";
import * as marked from "marked";

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

  return (
    <div>
      {/* Top greeting */}
      <Greetings
        imageLink={`${AppPaths.IMAGES}/${degree.imageName}`}
        heading={degreePageContent.greetingsHeading}
        text={degreePageContent.greetingsText}
      />
      <section className={"flex px-4 md:px-32 my-16"}>
        <div
          className={"px-4 lg:px-0"}
          dangerouslySetInnerHTML={{
            __html: marked.marked(degree.description),
          }}
        ></div>
        <div className={"bg-hoverGray flex flex-col h-fi"}>
          <h2 className={"my-header"}>Basic information</h2>
        </div>
      </section>
    </div>
  );
}
