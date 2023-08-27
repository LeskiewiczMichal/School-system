import { Greeting } from "../features/main-page";
import { useEffect, useState } from "react";
import { Article, ArticleCategory, ArticlesDisplay } from "../features/article";
import { useAppSelector } from "../hooks";
import { Link } from "react-router-dom";
import { ReactComponent as ArrowRight } from "../assets/icons/arrow/arrow-right-primary.svg";
import BigCardWithOptionalHeader from "../common_components/Card/BigCardWithOptionalHeader";
import ArticleRequest, {
  GetArticlesResponse,
} from "../features/article/services/ArticleRequest";
import TextAndButtonWithPhotoOnRight from "../common_components/Card/TextAndButtonWithPhotoOnRight";
import GlassBuilding from "../features/main-page/assets/glass-building.webp";
import { AppPaths } from "../App";
import { Sidebar } from "../features/sidebar";
import LoadingSpinner from "../common_components/LoadingSpinner";
import LoadingSpinnerPage from "./LoadingSpinnerPage";

export default function Home() {
  const links = useAppSelector((state) => state.links);
  const mobileNavView = useAppSelector(
    (state) => state.integration.mobileNavView,
  );
  const [scienceArticles, setScienceArticles] = useState<Article[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  // Get science articles
  useEffect(() => {
    const handleFetchArticles = async () => {
      // Prepare the link
      if (!links.articles.search) {
        return;
      }

      // Call the api
      const response: GetArticlesResponse = await ArticleRequest.getArticles({
        link: links.articles.search,
        category: ArticleCategory.NEWS,
        pagination: { size: 1 },
      });

      // Set the articles
      setScienceArticles(response.articles);
      setIsLoading(false);
    };

    handleFetchArticles();
  }, [links]);

  if (isLoading) {
    return <LoadingSpinnerPage />;
  }

  return (
    <main className="">
      <Greeting />
      <section className="flex flex-col px-4 md:px-32 my-16 gap-10">
        <ArticlesDisplay heading="NEWS" category={ArticleCategory.NEWS} />
        <Link
          to={AppPaths.ARTICLES}
          type="button"
          className="mb-16 w-2/3 lg:w-1/4 flex items-center border-4 border-primary gap-3 px-4 py-2 text-brandMain hover:border-brandMainActive hover:text-brandMainActive font-bold text-lg"
        >
          See more news from the Aquila University{" "}
          <ArrowRight className="w-6 h-6 " />
        </Link>
        <BigCardWithOptionalHeader
          title="IGNITING DISCOVERY: EXPLORING THE WONDERS OF SCIENCE"
          text="Embark on a thrilling scientific journey that unlocks the mysteries of
        the universe. Immerse yourself in cutting-edge research across various
        scientific disciplines, from astrophysics and quantum mechanics to
        biology and environmental sciences. Experience hands-on experiments,
        groundbreaking projects, and collaborative learning that ignite your
        passion for discovery and innovation."
          article={scienceArticles[0]}
        />
        <div className={"flex flex-col gap-10 lg:gap-7"}>
          <TextAndButtonWithPhotoOnRight
            heading={"THE TRANSFORMATIVE POWER OF HIGHER EDUCATION"}
            text={
              "Unlock your full potential through the transformative journey of higher education. Explore the research behind innovative teaching methodologies, personalized learning, and interdisciplinary programs. Embrace a holistic approach to education that fosters critical thinking, creativity, and adaptability."
            }
            buttonLink={"/research"}
            buttonText={"Learn More"}
            imageLink={GlassBuilding}
          />
          <ArticlesDisplay />
        </div>
      </section>
      {mobileNavView && <Sidebar />}
    </main>
  );
}
