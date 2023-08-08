import { Greeting } from "../features/main-page";
import { useEffect, useState } from "react";
import { Article, ArticleCategory, ArticlesDisplay } from "../features/article";
import { useAppSelector } from "../hooks";
import { Link } from "react-router-dom";
import { ReactComponent as ArrowRight } from "../assets/icons/arrow-right.svg";
import BigCardWithOptionalHeader from "../common_components/BigCardWithOptionalHeader";
import ArticleRequest from "../features/article/services/ArticleRequest";

export default function Home() {
  const links = useAppSelector((state) => state.links);
  const [scienceArticles, setScienceArticles] = useState<Article[]>([]);

  // Get science articles
  useEffect(() => {
    const handleFetchArticles = async () => {
      // Prepare the link
      if (!links.articlesSearch) {
        return;
      }

      // Call the api
      const articles: Article[] = await ArticleRequest.getArticles({
        link: links.articlesSearch,
        category: ArticleCategory.SCIENCE,
        pagination: { size: 1 },
      });

      // Set the articles
      setScienceArticles(articles);
    };

    handleFetchArticles();
  }, [links]);

  return (
    <main className="">
      <Greeting />
      <section className="flex flex-col px-4 md:px-32 my-16 gap-10">
        <ArticlesDisplay heading="NEWS" />
        <Link
          to="#"
          type="button"
          className="mb-16 w-2/3 lg:w-1/4 flex items-center border-4 border-primary gap-3 px-4 py-2 text-primary hover:border-primaryLighter hover:text-primaryLighter font-bold text-lg"
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
        />
      </section>
    </main>
  );
}

// Education text
// THE TRANSFORMATIVE POWER OF HIGHER EDUCATION
// Unlock your full potential through the transformative journey of higher education. Explore the research behind innovative teaching methodologies, personalized learning, and interdisciplinary programs. Embrace a holistic approach to education that fosters critical thinking, creativity, and adaptability, preparing you to thrive in an ever-changing world.
