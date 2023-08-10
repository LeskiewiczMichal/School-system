import Greetings from "../common_components/Card/Greetings";
import GlassBuilding from "../features/main-page/assets/glass-building.webp";
import { ArticleCategory, ArticlesDisplay } from "../features/article";
import LinkButtonBorderOnly from "../common_components/button/LinkButtonBorderOnly";
import { AppPaths } from "../App";

export default function Faculty() {
  return (
    <main>
      <Greetings
        imageLink={GlassBuilding} /// <---  HERE imageLink
        heading={"FACULTY OF SCIENCE"} /// <---  HERE heading
        text={
          /// <---  HERE text
          "The Faculty of Science carries out top-notch research on an international level. The instruction is developed and taught by leading scientists."
        }
        buttonText={"Read more about our research!"} /// <---  HERE buttonText
      />
      <section className="flex flex-col px-4 md:px-32 my-16 gap-10">
        <ArticlesDisplay
          heading="WELCOME TO THE SCIENCE CAMPUS" /// <---  HERE heading
          text={
            "Kumpula campus is a cluster of cutting-edge research and teaching of natural sciences. Read more about us, our research, and study opportunities."
          } /// <---  HERE text
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
