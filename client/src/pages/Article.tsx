import { Sidebar } from "../features/sidebar";
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAppSelector } from "../hooks";
import { ArticleRequest } from "../features/article";
import { Article as ArticleType } from "../features/article";

export default function Article() {
  const { id } = useParams<{ id: string }>();
  const articlesLinks = useAppSelector((state) => state.links.articles);
  const [article, setArticle] = useState<ArticleType | null>(null);

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
  }, [articlesLinks]);

  if (!article) {
    return <span>Loading</span>;
  }

  return (
    <div className={"flex h-full"}>
      <Sidebar />
      <main className={"lg:px-8 lg:pr-28 py-8"}>
        {/* Heading */}
        <h1 className="page-title_h1 text-brandMainNearlyBlack">
          {article.title}
        </h1>
        <p className={"text-grayscaleDarkText"}>{article.preview}</p>
      </main>
    </div>
  );
}
