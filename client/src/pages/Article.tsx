import { Sidebar } from "../features/sidebar";
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAppSelector } from "../hooks";

export default function Article() {
  const { id } = useParams<{ id: string }>();
  const articlesLinks = useAppSelector((state) => state.links.articles);
  const [article, setArticle] = useState<Article | null>(null);

  useEffect(() => {
    const handleFetchArticle = async () => {
      // Prepare the link
      if (!articlesLinks.getById) {
        return;
      }

      // Call the API
      const response =
    };

    handleFetchArticle();
  }, [articlesLinks]);

  if (!article) {
    return <span>Loading</span>;
  }

  return (
    <div className={"flex h-full"}>
      <Sidebar />
      <main className={"lg:px-8 py-8"}>
        {/* Heading */}
        <h1 className="page-title_h1 text-brandMainNearlyBlack">
          {article.title}
        </h1>
      </main>
    </div>
  );
}
