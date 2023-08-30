import { Sidebar } from "../features/sidebar";
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAppSelector } from "../hooks";
import {
  Article as ArticleType,
  ArticleRequest,
  ArticlesDisplay,
  ArticlesSidebar,
} from "../features/article";
import * as marked from "marked";
import { SidebarLinkProps } from "../features/sidebar/components/SidebarLink";
import { AppPaths } from "../App";
import LoadingSpinnerPage from "./LoadingSpinnerPage";

export default function Article() {
  const { id } = useParams<{ id: string }>();
  const { facultyId } = useParams<{ facultyId: string }>();
  const articlesLinks = useAppSelector((state) => state.links.articles);
  const [article, setArticle] = useState<ArticleType | null>(null);

  const sidebarLinks: SidebarLinkProps[] = [
    {
      title: "All Articles",
      redirectUrl: facultyId
        ? `/faculties/${facultyId}/articles?faculty=${facultyId}`
        : "/articles",
    },
  ];

  useEffect(() => {
    const handleFetchArticle = async () => {
      // Prepare the link
      if (!articlesLinks.getById || !id) {
        return;
      }

      // Call the API
      const response = await ArticleRequest.getFullArticle({
        link: articlesLinks.getById,
        id: id,
      });

      // Set the article
      setArticle(response);
    };

    handleFetchArticle();
  }, [articlesLinks, id, facultyId]);

  if (!article) {
    return <LoadingSpinnerPage />;
  }

  return (
    <div className={"flex h-full"}>
      <Sidebar links={sidebarLinks} backButtonLink={AppPaths.ARTICLES} />
      <main className={"pb-16 border-b border-grayscaleMediumDark"}>
        {/* Heading */}
        <section className={"px-4 lg:px-8 lg:pr-28 py-8 mb-4"}>
          <h1 className="page-title_h1 text-brandMainNearlyBlack">
            {article.title}
          </h1>

          {/* Preview */}
          <p className={"text-grayscaleDarkText"}>{article.preview}</p>
        </section>

        <div className={"flex flex-col xl:flex-row "}>
          {/* Sidebar on the right */}
          <ArticlesSidebar
            category={article.category}
            faculty={article.faculty?.name}
          />

          {/* Article content */}
          <section
            className={"grow px-0 lg:px-8 text-grayscaleDarkText xl:order-1"}
          >
            {article.imgPath && (
              <img src={article.imgPath} alt="Article" className={"mb-16"} />
            )}
            <div
              className={"px-4 lg:px-0 markdown-paragraph"}
              dangerouslySetInnerHTML={{
                __html: marked.marked(article.content!),
              }}
            ></div>
          </section>
        </div>

        {/* Related articles */}
        <section
          className={
            "px-4 lg:px-8 mt-16 pt-12 border-t border-grayscaleMediumDark"
          }
        >
          <ArticlesDisplay
            heading={"Related articles"}
            category={article.category}
          />
        </section>
      </main>
    </div>
  );
}
