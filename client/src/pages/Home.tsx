import { Greeting } from "../features/main-page";
import Card from "../common_components/Card";
import { useEffect, useState } from "react";
import { Article, ArticlesDisplay } from "../features/article";
import { useAppSelector } from "../hooks";

export default function Home() {
  const links = useAppSelector((state) => state.links);

  return (
    <main className="">
      <Greeting />
      <section className="flex flex-col px-4 md:px-32 my-32 gap-16">
        <ArticlesDisplay heading="NEWS" />
        <div className="col-span-3">
          <Card
            imageUrl="https://img.freepik.com/free-photo/harvard-university-cambridge-usa_1268-14363.jpg"
            imageAlt="building"
            redirectUrl="http://localhost:8080/articles"
            articleId="3"
            title={"Okay"}
            text="lorem ipsum lorem ipsum lorem ipsum lorem ipsum"
            wide
          />
        </div>
      </section>
    </main>
  );
}
