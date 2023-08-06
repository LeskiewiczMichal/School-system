import { Greeting } from "../features/main-page";
import Card from "../common_components/Card";
import { useEffect, useState } from "react";
import { Article, ArticlesDisplay } from "../features/article";
import { useAppSelector } from "../hooks";
import { Link } from "react-router-dom";
import { ReactComponent as ArrowRight } from "../assets/icons/arrow-right.svg";

export default function Home() {
  const links = useAppSelector((state) => state.links);

  return (
    <main className="">
      <Greeting />
      <section className="flex flex-col px-4 md:px-32 my-32 gap-12">
        <ArticlesDisplay heading="NEWS" />
        <Link
          to="#"
          type="button"
          className="self-end mb-16 flex items-center border-4 border-primary gap-3 px-4 py-2 text-primary hover:border-primaryLighter hover:text-primaryLighter font-bold text-lg"
        >
          See more news from the Aquila University{" "}
          <ArrowRight className="w-6 h-6 " />
        </Link>
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
