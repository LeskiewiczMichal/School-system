import { Sidebar } from "../../features/sidebar";
import ColoredBackgroundWithPhotoOnRight from "../../common_components/Card/ColoredBackgroundWithPhotoOnRight";
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
import { SidebarLinkProps } from "../../features/sidebar/components/SidebarLink";
import { WINDOW_WIDTH_CUSTOM_BREAKPOINT } from "../../utils/Constants";
import FacultyNavLinksCreator, {
  PageType,
} from "../../features/faculty/FacultyNavLinksCreator";

export default function TeachingAndStudying() {
  const links = useAppSelector((state) => state.links);
  const facultyId = useParams().facultyId;
  const [articles, setArticles] = useState<Article[]>([]);
  const [mobileNavView, setMobileNavView] = useState<boolean>(
    window.innerWidth <= WINDOW_WIDTH_CUSTOM_BREAKPOINT,
  );

  // Get the page content
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
    };

    handleFetchArticles();
  }, [links, facultyId]);

  useEffect(() => {
    // Set the mobile nav view
    const handleResize = () => {
      if (window.innerWidth <= WINDOW_WIDTH_CUSTOM_BREAKPOINT) {
        setMobileNavView(true);
      } else {
        setMobileNavView(false);
      }
    };

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  return (
    <div className={"flex h-full"}>
      <main className={"h-full w-full flex flex-col lg:px-8 py-8"}>
        <section className={"w-full flex flex-col mb-24"}>
          {/* Title and text */}
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
                    redirectUrl={
                      facultyId
                        ? `/faculties/${facultyId}/articles}`
                        : AppPaths.ARTICLES
                    }
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
