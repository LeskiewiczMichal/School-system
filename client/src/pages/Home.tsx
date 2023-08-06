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
        <div className="col-span-3">
          <h4 className="mb-8 text-4xl font-bold">
            IGNITING DISCOVERY: EXPLORING THE WONDERS OF SCIENCE
          </h4>
          <p className="mb-6">
            Embark on a thrilling scientific journey that unlocks the mysteries
            of the universe. Immerse yourself in cutting-edge research across
            various scientific disciplines, from astrophysics and quantum
            mechanics to biology and environmental sciences. Experience hands-on
            experiments, groundbreaking projects, and collaborative learning
            that ignite your passion for discovery and innovation.
          </p>

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

// Education text
// THE TRANSFORMATIVE POWER OF HIGHER EDUCATION
// Unlock your full potential through the transformative journey of higher education. Explore the research behind innovative teaching methodologies, personalized learning, and interdisciplinary programs. Embrace a holistic approach to education that fosters critical thinking, creativity, and adaptability, preparing you to thrive in an ever-changing world.
