import { Sidebar } from "../../features/sidebar";
import { SidebarLinkProps } from "../../features/sidebar/components/SidebarLink";
import FacultyNavLinksCreator, {
  PageType,
} from "../../features/faculty/FacultyNavLinksCreator";
import { useAppSelector } from "../../hooks";
import { useParams } from "react-router-dom";
import ResearchPageContentInterface from "./ResearchPageContentInterface";
import { useEffect, useState } from "react";
import ArticleRequest, {
  GetArticlesResponse,
} from "../../features/article/services/ArticleRequest";
import {
  Article,
  ArticleCategory,
  ArticlesDisplay,
} from "../../features/article";
import Card from "../../common_components/Card/Card";
import FullWidthColoredBackground from "../../common_components/Card/FullWidthColoredBackground";
import * as marked from "marked";
import { AppPaths } from "../../App";

export default function Research() {
  const links = useAppSelector((state) => state.links);
  const mobileNavView = useAppSelector(
    (state) => state.integration.mobileNavView,
  );
  const facultyId = useParams().facultyId;
  const [articles, setArticles] = useState<Article[]>([]);

  // Get the page content
  const researchPageContent: ResearchPageContentInterface = facultyId
    ? require(`./json/research-${facultyId}.json`)
    : require(`./json/research.json`);

  useEffect(() => {
    const handleFetchArticles = async () => {
      // Prepare the link
      if (!links.articles.search) {
        return;
      }

      // Call the api
      const response: GetArticlesResponse = await ArticleRequest.getArticles({
        link: links.articles.search,
        category: ArticleCategory.SCIENCE,
        faculty: facultyId || undefined,
        pagination: { size: 6 },
      });

      // Set the articles
      setArticles(response.articles);
    };

    handleFetchArticles();
  }, [links, facultyId]);

  const sidebarLinks: SidebarLinkProps[] = [
    {
      title: "Teaching",
      redirectUrl: "/teaching",
      active: false,
    },
    {
      title: "Academic Staff",
      redirectUrl: "/academic-staff",
      active: false,
    },
    {
      title: "Research",
      redirectUrl: "/research",
      active: true,
    },
  ];

  return (
    <div className={"flex h-full"}>
      {/* Sidebar */}
      {!facultyId && <Sidebar links={sidebarLinks} />}
      <main className={"h-full w-full flex flex-col py-8"}>
        {/* Title and text */}
        <section className={`${facultyId ? "lg:px-20" : "lg:px-8"}`}>
          <h1 className="page-title_h1 px-2 lg:px-0 text-brandMainNearlyBlack">
            {researchPageContent.title}
          </h1>
          <p
            className={"markdown-paragraph text-grayscaleDarkText px-2 lg:px-0"}
            dangerouslySetInnerHTML={{
              __html: marked.marked(researchPageContent.textUnderTitle),
            }}
          ></p>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 mb-16  gap-4">
            {articles.map((article) => {
              return (
                <Card
                  title={article.title}
                  text={article.preview}
                  imageUrl={article.imgPath}
                  imageAlt="Article Preview"
                  articleId={article.id.toString()}
                  redirectUrl={
                    facultyId
                      ? `/faculties/${facultyId}/articles`
                      : AppPaths.ARTICLES
                  }
                  key={article.id.toString()}
                />
              );
            })}
          </div>
        </section>
        <FullWidthColoredBackground
          color={"brandMain"}
          textColor={"white"}
          heading={researchPageContent.coloredBgCardHeading}
          text={researchPageContent.coloredBgCardText}
          buttonText={researchPageContent.coloredBgCardButtonText}
          buttonLink={researchPageContent.coloredBgCardButtonLink}
        />
      </main>
    </div>
  );
}
