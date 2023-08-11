import { Sidebar } from "../../features/sidebar";
import DarkBackgroundWithPhotoOnRight from "../../common_components/Card/DarkBackgroundWithPhotoOnRight";
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

export default function TeachingAndStudying() {
  const links = useAppSelector((state) => state.links);
  const facultyId = useParams().facultyId;
  const [articles, setArticles] = useState<Article[]>([]);
  // const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    const handleFetchArticles = async () => {
      // Prepare the link
      if (!links.articles.search || !facultyId) {
        return;
      }

      // Call the api
      const response: GetArticlesResponse = await ArticleRequest.getArticles({
        link: links.articles.search,
        category: ArticleCategory.SCIENCE,
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
            TEACHING AT THE FACULTY
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
          <div className={"flex mb-8 w-full items-center"}>
            {/*<MyHeading heading={"Latest articles about science"} />*/}
            <h4 className="my-header text-brandMainNearlyBlack">
              Latest articles about science
            </h4>
            <div className={"h-[1px] grow bg-black ml-6"}></div>
          </div>
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
