import { Sidebar } from "../sidebar";
import { useEffect, useState } from "react";
import { Article, ArticleCategory } from "../features/article";
import { SidebarButtonProps } from "../sidebar/components/SidebarButton";
import { useAppSelector } from "../hooks";
import ArticleRequest from "../features/article/services/ArticleRequest";
import DarkBackgroundWithPhotoOnRight from "../common_components/Card/DarkBackgroundWithPhotoOnRight";
import GroupOfStundetsPhoto from "../features/article/assets/group.webp";
import Card from "../common_components/Card/Card";
import MyHeading from "../common_components/MyHeading";

export default function Articles() {
  const links = useAppSelector((state) => state.links);
  const [articleCategory, setArticleCategory] = useState<ArticleCategory>(
    ArticleCategory.NEWS,
  );
  const [articles, setArticles] = useState<Article[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  const sidebarButtons: SidebarButtonProps[] = [
    {
      text: "News",
      onClick: () => {
        setArticleCategory(ArticleCategory.NEWS);
      },
      active: articleCategory === ArticleCategory.NEWS,
    },
    {
      text: "Events",
      onClick: () => {
        setArticleCategory(ArticleCategory.EVENTS);
      },
      active: articleCategory === ArticleCategory.EVENTS,
    },
    {
      text: "Sport",
      onClick: () => {
        setArticleCategory(ArticleCategory.SPORT);
      },
      active: articleCategory === ArticleCategory.SPORT,
    },
    {
      text: "Science",
      onClick: () => {
        setArticleCategory(ArticleCategory.SCIENCE);
      },
      active: articleCategory === ArticleCategory.SCIENCE,
    },
    {
      text: "Other",
      onClick: () => {
        setArticleCategory(ArticleCategory.OTHER);
      },
      active: articleCategory === ArticleCategory.OTHER,
    },
  ];

  useEffect(() => {
    const handleFetchArticles = async () => {
      // Prepare the link
      if (!links.articlesSearch) {
        return;
      }

      // Call the api
      const articles: Article[] = await ArticleRequest.getArticles({
        link: links.articlesSearch,
        category: articleCategory,
        pagination: { size: 9 },
      });

      // Set the articles
      setArticles(articles);
      setIsLoading(false);
    };

    handleFetchArticles();
  }, [articleCategory]);

  return (
    <div className={"flex h-full"}>
      <Sidebar buttons={sidebarButtons} />
      <main className={"h-full w-full flex flex-col lg:px-8 py-8"}>
        <section className={"w-full flex flex-col mb-24"}>
          <h1 className="page-title_h1 text-brandMainNearlyBlack">
            {articleCategory}
          </h1>
          <DarkBackgroundWithPhotoOnRight
            heading={"Aquila's news"}
            text={"Checkout out latest news from our University"}
            buttonLink={"/"}
            buttonText={"See what is new in our faculties"}
            imageLink={GroupOfStundetsPhoto}
          />
        </section>
        <section className={"flex flex-col w-full px-8"}>
          <MyHeading heading={"LATEST ARTICLES"} />
          <div
            className={"grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4"}
          >
            {articles.map((article) => {
              return (
                <Card
                  key={article.id.toString()}
                  title={article.title}
                  text={article.preview}
                  imageUrl={article.imgPath}
                  imageAlt={"Article preview photo"}
                  articleId={article.id.toString()}
                  redirectUrl={"#"}
                />
              );
            })}
          </div>
        </section>
      </main>
    </div>
  );
}
