import { Sidebar } from "../sidebar";
import { SidebarLinkProps } from "../sidebar/components/SidebarLink";
import { useEffect, useState } from "react";
import { Article, ArticleCategory } from "../features/article";
import { SidebarButtonProps } from "../sidebar/components/SidebarButton";
import { useAppSelector } from "../hooks";
import ArticleRequest from "../features/article/services/ArticleRequest";

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
        pagination: { size: 10 },
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
      <main className={"h-full w-full flex flex-col px-4"}>
        qewr
        <p>qwer</p>
        <p>qwer</p>
        <p>qwerdfds</p>
        <p>qwer</p>
        <p>qwer</p>
        <p>qwerdfds</p>
        <p>qwer</p>
        <p>qwer</p>
        <p>qwerdfds</p>
        <p>qwer</p>
        <p>qwer</p>
        <p>qwerdfds</p>
        <p>qwer</p>
        <p>qwer</p>
        <p>qwerdfds</p>
        <p>qwer</p>
        <p>qwer</p>
        <p>qwerdfds</p>
        <p>qwer</p>
        <p>qwer</p>
        <p>qwerdfds</p>
        <p>qwer</p>
        <p>qwer</p>
        <p>qwerdfds</p>
        <p>qwer</p>
        <p>qwer</p>
        <p>qwerdfds</p>
        <p>qwer</p>
        <p>qwer</p>
        <p>qwerdfds</p>
        <p>qwer</p>
        <p>qwer</p>
        <p>qwerdfds</p>
      </main>
    </div>
  );
}
