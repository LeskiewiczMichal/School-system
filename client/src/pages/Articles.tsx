import { Sidebar } from "../features/sidebar";
import { useEffect, useState } from "react";
import { Article, ArticleCategory } from "../features/article";
import { SidebarButtonProps } from "../features/sidebar/components/SidebarButton";
import { useAppSelector } from "../hooks";
import ArticleRequest, {
  GetArticlesResponse,
} from "../features/article/services/ArticleRequest";
import ColoredBackgroundWithPhotoOnRight from "../common_components/Card/ColoredBackgroundWithPhotoOnRight";
import GroupOfStundetsPhoto from "../features/article/assets/group.webp";
import Card from "../common_components/Card/Card";
import MyHeading from "../common_components/MyHeading";
import PaginationInfo from "../type/PaginationInfo";
import { AppPaths } from "../App";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import PaginationButtons from "../common_components/PaginationButtons";

export default function Articles() {
  const navigate = useNavigate();
  const links = useAppSelector((state) => state.links);
  const location = useLocation();
  const facultyId = useParams().facultyId;

  // Query parameters
  const queryParams = new URLSearchParams(location.search);
  const category =
    ArticleCategory[
      queryParams.get("category") as keyof typeof ArticleCategory
    ] || ArticleCategory.NEWS;
  const faculty = queryParams.get("faculty");

  const [articles, setArticles] = useState<Article[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [page, setPage] = useState<number>(0);
  const [paginationInfo, setPaginationInfo] = useState<PaginationInfo>({
    page: 0,
    size: 0,
    totalElements: 0,
    totalPages: 0,
  });

  const changeCategory = (newCategory: ArticleCategory) => {
    let link = `/articles?category=${newCategory}`;
    if (facultyId) {
      link = `/faculties/${facultyId}/articles?category=${newCategory}`;
    }
    if (faculty) {
      link += `&faculty=${faculty}`;
    }
    navigate(link);
  };

  const sidebarButtons: SidebarButtonProps[] = [
    {
      text: "News",
      onClick: () => {
        changeCategory(ArticleCategory.NEWS);
      },
      active: category === ArticleCategory.NEWS,
    },
    {
      text: "Events",
      onClick: () => {
        changeCategory(ArticleCategory.EVENTS);
      },
      active: category === ArticleCategory.EVENTS,
    },
    {
      text: "Sport",
      onClick: () => {
        changeCategory(ArticleCategory.SPORT);
      },
      active: category === ArticleCategory.SPORT,
    },
    {
      text: "Science",
      onClick: () => {
        changeCategory(ArticleCategory.SCIENCE);
      },
      active: category === ArticleCategory.SCIENCE,
    },
    {
      text: "Other",
      onClick: () => {
        changeCategory(ArticleCategory.OTHER);
      },
      active: category === ArticleCategory.OTHER,
    },
  ];

  useEffect(() => {
    const handleFetchArticles = async () => {
      // Prepare the link
      if (!links.articles.search) {
        return;
      }

      // Call the api
      const response: GetArticlesResponse = await ArticleRequest.getArticles({
        link: links.articles.search,
        category: category,
        faculty: faculty || undefined,
        pagination: { size: 9, page: page },
      });

      // Set the articles
      setArticles(response.articles);
      setPaginationInfo(response.paginationInfo);
      setIsLoading(false);
    };

    handleFetchArticles();
  }, [category, page, links]);

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
            {category}
          </h1>
          {/* Informative Card */}
          <ColoredBackgroundWithPhotoOnRight
            heading={"Aquila's news"}
            text={"Checkout out latest news from our University"}
            buttonLink={"/faculties"}
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
                  redirectUrl={AppPaths.ARTICLES}
                />
              );
            })}
          </div>

          {/* Pagination buttons */}
          <PaginationButtons
            paginationInfo={paginationInfo}
            page={page}
            changePage={changePage}
          />
        </section>
      </main>
    </div>
  );
}
