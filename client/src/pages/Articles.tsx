import { Sidebar } from "../sidebar";
import { SidebarLinkProps } from "../sidebar/components/SidebarLink";
import { useState } from "react";
import { ArticleCategory } from "../features/article";
import { SidebarButtonProps } from "../sidebar/components/SidebarButton";

export default function Articles() {
  const [articleCategory, setArticleCategory] = useState<ArticleCategory>(
    ArticleCategory.NEWS,
  );

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

  return (
    <div className={"flex h-full"}>
      <Sidebar buttons={sidebarButtons} />
      <main className={"h-full flex flex-col px-4"}>
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
