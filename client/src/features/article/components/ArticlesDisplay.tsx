import Card from "../../../common_components/Card/Card";
import ArticleCategory from "../Types/ArticleCategory";
import { useEffect, useState } from "react";
import Article from "../Types/Article";
import { useAppSelector } from "../../../hooks";
import APILink from "../../../type/APILink";
import ArticleRequest, {
  GetArticlesResponse,
} from "../services/ArticleRequest";
import { AppPaths } from "../../../App";

interface ArticlesDisplayProps {
  heading?: string;
  text?: string;
  faculty?: string;
  category?: ArticleCategory;
}

export default function ArticlesDisplay(props: ArticlesDisplayProps) {
  const { heading, faculty = null, category = null, text } = props;
  const [articles, setArticles] = useState<Article[]>([]);
  const links = useAppSelector((state) => state.links);

  useEffect(() => {
    const handleFetchArticles = async () => {
      // Prepare the link
      let link: APILink | null = links.articles.getArticles;
      if (faculty || category) {
        link = links.articles.search;
      }
      if (!link) {
        return;
      }

      // Call the api
      const response: GetArticlesResponse = await ArticleRequest.getArticles({
        link,
        faculty: faculty ? faculty : undefined,
        category: category ? category : undefined,
        pagination: { size: 3 },
      });

      // Set the articles
      setArticles(response.articles);
    };

    handleFetchArticles();
  }, [links]);

  return (
    <section className="flex flex-col">
      {heading ? (
        <h4 className="my-header text-brandMainNearlyBlack">{heading}</h4>
      ) : null}
      {text && <p className={"text-grayscaleDarkText mb-6"}>{text}</p>}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 mt-8  gap-4">
        {articles.map((article) => {
          return (
            <Card
              title={article.title}
              text={article.preview}
              imageUrl={article.imgPath}
              imageAlt="Article Preview"
              articleId={article.id.toString()}
              redirectUrl={
                faculty ? `/faculties/${faculty}/articles` : AppPaths.ARTICLES
              }
              key={article.id.toString()}
            />
          );
        })}
      </div>
    </section>
  );
}
