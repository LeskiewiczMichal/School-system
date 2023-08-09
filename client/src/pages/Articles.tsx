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
import { ReactComponent as ChevronLeft } from "../assets/icons/chevron/chevron-left.svg";
import { ReactComponent as ChevronRight } from "../assets/icons/chevron/chevron-right.svg";

export default function Articles() {
  const links = useAppSelector((state) => state.links);
  const [articleCategory, setArticleCategory] = useState<ArticleCategory>(
    ArticleCategory.NEWS,
  );
  const [articles, setArticles] = useState<Article[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [page, setPage] = useState<number>(0);

  const sidebarButtons: SidebarButtonProps[] = [
    {
      text: "News",
      onClick: () => {
        setArticleCategory(ArticleCategory.NEWS);
        setPage(0);
      },
      active: articleCategory === ArticleCategory.NEWS,
    },
    {
      text: "Events",
      onClick: () => {
        setArticleCategory(ArticleCategory.EVENTS);
        setPage(0);
      },
      active: articleCategory === ArticleCategory.EVENTS,
    },
    {
      text: "Sport",
      onClick: () => {
        setArticleCategory(ArticleCategory.SPORT);
        setPage(0);
      },
      active: articleCategory === ArticleCategory.SPORT,
    },
    {
      text: "Science",
      onClick: () => {
        setArticleCategory(ArticleCategory.SCIENCE);
        setPage(0);
      },
      active: articleCategory === ArticleCategory.SCIENCE,
    },
    {
      text: "Other",
      onClick: () => {
        setArticleCategory(ArticleCategory.OTHER);
        setPage(0);
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
        pagination: { size: 9, page: page },
      });

      // Set the articles
      setArticles(articles);
      setIsLoading(false);
    };

    handleFetchArticles();
  }, [articleCategory, page, links]);

  // TODO: add pagination info to the response
  const changePage = (direction: "next" | "previous") => {
    setPage((prevPage) => {
      if (direction === "next") {
        return prevPage + 1;
      } else {
        return prevPage - 1;
      }
    });
  };

  return (
    <div className={"flex h-full"}>
      <Sidebar buttons={sidebarButtons} />
      <main className={"h-full w-full flex flex-col lg:px-8 py-8"}>
        {/*Heading*/}
        <section className={"w-full flex flex-col mb-24"}>
          <h1 className="page-title_h1 text-brandMainNearlyBlack">
            {articleCategory}
          </h1>
          {/* Informative Card */}
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
          {/* Articles */}
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
          {/* Pagination buttons */}
          <div className={"flex w-full items-center justify-end pt-6 gap-8"}>
            <button
              type={"button"}
              onClick={() => changePage("previous")}
              className={
                "flex items-center font-bold w-32 justify-center border-brandMainNearlyBlack text-brandMainNearlyBlack pr-2 py-2"
              }
            >
              <ChevronLeft className={"h-8 w-8"} /> Previous
            </button>
            <button
              type={"button"}
              onClick={() => changePage("next")}
              className={
                "flex items-center font-bold w-32 justify-center border-brandMainNearlyBlack text-brandMainNearlyBlack pl-2 py-2"
              }
            >
              Next <ChevronRight className={"h-8 w-8"} />
            </button>
          </div>
        </section>
      </main>
    </div>
  );
}
