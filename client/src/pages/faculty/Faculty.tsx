import Greetings from "../../common_components/Card/Greetings";
import LinkButtonBorderOnly from "../../common_components/button/LinkButtonBorderOnly";
import { useParams } from "react-router-dom";
import FacultyPageContentInterface from "./FacultyPageContentInterface";
import BigCardWithOptionalHeader from "../../common_components/Card/BigCardWithOptionalHeader";
import Card from "../../common_components/Card/Card";
import { useEffect, useState } from "react";
import Article from "../../features/article/Types/Article";
import { useAppSelector } from "../../hooks";
import APILink from "../../type/APILink";
import ArticleRequest, {
  GetArticlesResponse,
} from "../../features/article/services/ArticleRequest";

export default function Faculty() {
  const { facultyId } = useParams<{ facultyId: string }>();
  const [articles, setArticles] = useState<Article[]>([]);
  const links = useAppSelector((state) => state.links);

  useEffect(() => {
    const handleFetchArticles = async () => {
      // Prepare the link
      let link: APILink | null = links.articles.search;
      if (!link) {
        return;
      }

      // Call the api
      const response: GetArticlesResponse = await ArticleRequest.getArticles({
        link,
        faculty: facultyId,
        // category: category ? category : undefined,
        pagination: { size: 8 },
      });

      // Set the articles
      setArticles(response.articles);
    };

    handleFetchArticles();
  }, [links, facultyId]);

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
          {/*<ArticlesDisplay*/}
          <section className="flex flex-col">
            <h4 className="my-header text-brandMainNearlyBlack">
              {facultyPageContent.newsDisplayHeading}
            </h4>

            <p className={"text-grayscaleDarkText mb-2"}>
              {facultyPageContent.newsDisplayText}
            </p>

            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 mt-6  gap-4">
              {articles.slice(0, 3).map((article) => {
                return (
                  <Card
                    title={article.title}
                    text={article.preview}
                    imageUrl={article.imgPath}
                    imageAlt="Article Preview"
                    articleId={article.id.toString()}
                    redirectUrl={`/faculties/${facultyId}/articles`}
                    key={article.id.toString()}
                  />
                );
              })}
            </div>
          </section>

          {/* News button */}
          <LinkButtonBorderOnly
            text={"All news"}
            link={`/faculties/${facultyId}/articles?faculty=${facultyId}`}
            color={"brandMain"}
            width={"w-2/3 lg:w-1/4"}
          />
        </div>

        <div className={"flex flex-col"}>
          {/* Articles */}
          <section className="flex flex-col">
            <h4 className="my-header text-brandMainNearlyBlack">
              {facultyPageContent.newsDisplayHeading}
            </h4>

            <p className={"text-grayscaleDarkText mb-2"}>
              {facultyPageContent.newsDisplayText}
            </p>

            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 mt-6 mb-8  gap-4">
              {articles.slice(3, 6).map((article) => {
                return (
                  <Card
                    title={article.title}
                    text={article.preview}
                    imageUrl={article.imgPath}
                    imageAlt="Article Preview"
                    articleId={article.id.toString()}
                    redirectUrl={`/faculties/${facultyId}/articles`}
                    key={article.id.toString()}
                  />
                );
              })}
            </div>
          </section>

          <div className={"mb-6"}>
            {/* Big card articles */}
            {articles.length >= 7 && (
              <BigCardWithOptionalHeader
                article={articles[6]}
                facultyId={facultyId}
              />
            )}
          </div>
          {articles.length >= 8 && (
            <BigCardWithOptionalHeader
              article={articles[7]}
              facultyId={facultyId}
            />
          )}
        </div>
      </section>
    </main>
  );
}
