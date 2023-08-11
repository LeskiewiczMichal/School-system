import { Sidebar } from "../../features/sidebar";
import ColoredBackgroundWithPhotoOnRight from "../../common_components/Card/ColoredBackgroundWithPhotoOnRight";
import GroupOfStundetsPhoto from "../../features/article/assets/group.webp";
import MyHeading from "../../common_components/MyHeading";
import Card from "../../common_components/Card/Card";
import { AppPaths } from "../../App";
import { useEffect, useState } from "react";
import ArticleRequest, {
  GetArticlesResponse,
} from "../../features/article/services/ArticleRequest";
import { useAppSelector } from "../../hooks";
import { Article, ArticleCategory } from "../../features/article";
import { useParams } from "react-router-dom";
import BigCardWithOptionalHeader from "../../common_components/Card/BigCardWithOptionalHeader";
import MyHeadingWithLine from "../../common_components/MyHeadingWithLine";
import TeachingAndStudyingPageContentInterface from "./TeachingAndStudyingPageContentInterface";

export default function TeachingAndStudying() {
  const links = useAppSelector((state) => state.links);
  const facultyId = useParams().facultyId;
  const [articles, setArticles] = useState<Article[]>([]);
  // const [isLoading, setIsLoading] = useState<boolean>(true);

  const teachingAndStudyingPageContent: TeachingAndStudyingPageContentInterface = require(`./json/teachingAndStudying-${facultyId}.json`);

  useEffect(() => {
    const handleFetchArticles = async () => {
      // Prepare the link
      if (!links.articles.search || !facultyId) {
        return;
      }

      // Call the api
      const response: GetArticlesResponse = await ArticleRequest.getArticles({
        link: links.articles.search,
        category: ArticleCategory.EVENTS,
        faculty: facultyId,
        pagination: { size: 4 },
      });

      // Set the articles
      setArticles(response.articles);
      // setIsLoading(false);
    };

    handleFetchArticles();
  }, [links, facultyId]);

  return (
    <div className={"flex h-full"}>
      <Sidebar />
      <main className={"h-full w-full flex flex-col lg:px-8 py-8"}>
        <section className={"w-full flex flex-col mb-24"}>
          <h1 className="page-title_h1 text-brandMainNearlyBlack">
            {teachingAndStudyingPageContent.title}
          </h1>
          <p className={"text-grayscaleDarkText mb-6"}>
            {teachingAndStudyingPageContent.textUnderTitle}
          </p>
          {/* Informative Card */}
          <ColoredBackgroundWithPhotoOnRight
            heading={teachingAndStudyingPageContent.cardHeading}
            text={teachingAndStudyingPageContent.cardText}
            buttonLink={teachingAndStudyingPageContent.cardButtonLink}
            buttonText={teachingAndStudyingPageContent.cardButtonText}
            imageLink={require(`../assets/${teachingAndStudyingPageContent.cardImage}`)}
            backgroundColor={teachingAndStudyingPageContent.cardBackgroundColor}
          />
        </section>
        <section className={"flex flex-col w-full px-8"}>
          <MyHeadingWithLine
            heading={teachingAndStudyingPageContent.articlesHeading}
          />
          {/* Articles */}
          {articles.length !== 0 && (
            <div
              className={"grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4"}
            >
              <BigCardWithOptionalHeader article={articles[3]} />
              {articles.slice(1).map((article) => {
                return (
                  <Card
                    key={article.id.toString()}
                    title={article.title}
                    text={article.preview}
                    imageUrl={article.imgPath}
                    imageAlt={"Article preview photo"}
                    articleId={article.id.toString()}
                    redirectUrl={AppPaths.ARTICLES}
                  />
                );
              })}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}
