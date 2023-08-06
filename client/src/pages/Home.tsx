import { Greeting } from "../features/main-page";
import Card from "../common_components/Card";
import { useState } from "react";
import { Article } from "../features/article";

export default function Home() {
  const [articles, setArticles] = useState<Article[]>([]);

  return (
    <main className="">
      <Greeting />
      <section className="flex flex-col px-4 md:px-32 my-32 gap-16">
        <div className="grid grid-cols-1 lg:grid-cols-3  gap-4">
          <Card
            imageUrl="https://img.freepik.com/free-photo/harvard-university-cambridge-usa_1268-14363.jpg"
            imageAlt="building"
            redirectUrl="http://localhost:8080/articles"
            articleId="3"
            title={"Okay"}
            text="lorem ipsum lorem ipsum lorem ipsum lorem ipsum"
          />
          <Card
            imageUrl="https://img.freepik.com/free-photo/harvard-university-cambridge-usa_1268-14363.jpg"
            imageAlt="building"
            redirectUrl="http://localhost:8080/articles"
            articleId="3"
            title={"Okay"}
            text="lorem ipsum lorem ipsum lorem ipsum lorem ipsum"
          />
          <Card
            imageUrl="https://img.freepik.com/free-photo/harvard-university-cambridge-usa_1268-14363.jpg"
            imageAlt="building"
            redirectUrl="http://localhost:8080/articles"
            articleId="3"
            title={"Okay"}
            text="lorem ipsum lorem ipsum lorem ipsum lorem ipsum em ipsum lorem ipsum lorem ipsum lorem ipsum em ipsum lorem ipsum lorem ipsum lorem ipsum"
          />
        </div>
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
