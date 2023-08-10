import Greetings from "../../common_components/Card/Greetings";
import GlassBuilding from "../../features/main-page/assets/glass-building.webp";
import { ArticleCategory, ArticlesDisplay } from "../../features/article";
import LinkButtonBorderOnly from "../../common_components/button/LinkButtonBorderOnly";
import { AppPaths } from "../../App";
import { useParams } from "react-router-dom";
import FacultyPageContentInterface from "../types/FacultyPageContentInterface";

export default function Faculty() {
  const { facultyId } = useParams<{ facultyId: string }>();

  const facultyPageContent: FacultyPageContentInterface = require(`./json/faculty-${facultyId}.json`);

  return (
    <main>
      <Greetings
        imageLink={require(`../assets/${facultyPageContent.greetingsImageLink}`)}
        heading={facultyPageContent.greetingsHeading}
        text={facultyPageContent.greetingsText}
        buttonText={facultyPageContent.greetingsButtonText}
      />
      <section className="flex flex-col px-4 md:px-32 my-16 gap-10">
        <ArticlesDisplay
          heading={facultyPageContent.newsDisplayHeading} /// <---  HERE heading
          text={facultyPageContent.newsDisplayText} /// <---  HERE text
          category={ArticleCategory.NEWS}
          faculty={"101"} /// <---  HERE must be faculty id
        />
        <LinkButtonBorderOnly
          text={"All news"}
          link={AppPaths.ARTICLES}
          color={"brandMain"}
          width={"w-2/3 lg:w-1/4"}
        />
      </section>
    </main>
  );
}
