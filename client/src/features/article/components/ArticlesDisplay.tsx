import Card from "../../../common_components/Card";
import NullableString from "../../../type/NullableString";
import ArticleCategory from "../Types/ArticleCategory";
import { useEffect, useState } from "react";
import RequestService from "../../../utils/RequestService";
import Article from "../Types/Article";
import { useAppSelector } from "../../../hooks";
import { SortDirection } from "../../../type/PaginationParams";
import APILink from "../../../type/APILink";
import ArticleMapper from "../mapper/ArticleMapper";

interface ArticlesDisplayProps {
  heading: string;
  faculty?: string;
  category?: ArticleCategory;
}

export default function ArticlesDisplay(props: ArticlesDisplayProps) {
  const { heading, faculty = null, category = null } = props;
  const [articles, setArticles] = useState<Article[]>([]);
  const links = useAppSelector((state) => state.links);

  useEffect(() => {
    const handleFetchArticles = async () => {
      // Prepare the link
      let link: APILink | null = links.articles;
      let params = {};
      if (faculty) {
        link = links.articlesSearch;
        params = {
          ...params,
          faculty: faculty,
        };
      }
      if (category) {
        link = links.articlesSearch;
        params = {
          ...params,
          category: category,
        };
      }

      if (!link) {
        return;
      }

      // Call the API
      const responseData = await RequestService.performGetRequest({
        link: link,
        pagination: {
          page: 0,
          size: 3,
          sort: ["id", SortDirection.ASC],
        },
        params: params,
      });

      // Convert the response data into articles
      const articlesArr: Article[] =
        ArticleMapper.mapArticleArrayFromServerData(
          responseData._embedded.articles,
        );
      setArticles(articlesArr);
    };

    handleFetchArticles();
  }, [links]);

  return (
    <section className="flex flex-col">
      <h4 className="mb-8 text-4xl font-bold">{heading}</h4>
      <div className="grid grid-cols-1 lg:grid-cols-3  gap-4">
        {articles.map((article) => {
          return (
            <Card
              title={article.title}
              text={article.preview}
              imageUrl={article.imgPath}
              imageAlt="Article Preview"
              articleId={article.id.toString()}
              redirectUrl="red"
              key={article.id.toString()}
            />
          );
        })}
      </div>
    </section>
  );
}
