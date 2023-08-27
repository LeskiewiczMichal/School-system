import Greetings from "../../common_components/Card/Greetings";
import { ArticleCategory, ArticlesDisplay } from "../../features/article";
import LinkButtonBorderOnly from "../../common_components/button/LinkButtonBorderOnly";
import { AppPaths } from "../../App";
import { useParams } from "react-router-dom";
import FacultyPageContentInterface from "./FacultyPageContentInterface";
import BigCardWithOptionalHeader from "../../common_components/Card/BigCardWithOptionalHeader";

export default function Faculty() {
  const { facultyId } = useParams<{ facultyId: string }>();

  const facultyPageContent: FacultyPageContentInterface = require(`./json/faculty-${facultyId}.json`);

  return (
    <main>
      {/* Top greeting */}
      <Greetings
        imageLink={require(`../assets/${facultyPageContent.greetingsImageLink}`)}
        heading={facultyPageContent.greetingsHeading}
        text={facultyPageContent.greetingsText}
        buttonText={facultyPageContent.greetingsButtonText}
      />
      <section className="flex flex-col px-4 md:px-32 my-16">
        {/*  News */}
        <div className={"mb-24 flex flex-col gap-8"}>
          <ArticlesDisplay
            heading={facultyPageContent.newsDisplayHeading}
            text={facultyPageContent.newsDisplayText}
            category={ArticleCategory.NEWS}
            faculty={"101"}
          />
          {/* News button */}
          <LinkButtonBorderOnly
            text={"All news"}
            link={`/faculties/${facultyId}/articles?faculty=${facultyId}`}
            color={"brandMain"}
            width={"w-2/3 lg:w-1/4"}
          />
        </div>
        <div className={"flex flex-col"}>
          {/*// TODO: REPLACE WITH INFORMATION */}
          <ArticlesDisplay
            heading={facultyPageContent.informationHeading}
            category={ArticleCategory.NEWS}
            faculty={"101"}
          />
          {/*  TODO: ADD TWO BIG CARDS WITH INFO  */}
          TODO: ADD TWO BIG CARDS WITH INFO TODO: ADD TWO BIG CARDS WITH INFO
          TODO: ADD TWO BIG CARDS WITH INFO TODO: ADD TWO BIG CARDS WITH INFO
          TODO: ADD TWO BIG CARDS WITH INFO
        </div>
      </section>
    </main>
  );
}
