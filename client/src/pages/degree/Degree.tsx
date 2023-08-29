import Greetings from "../../common_components/Card/Greetings";
import { useEffect, useState } from "react";
import {
  Degree as DegreeType,
  DegreeBasicInformation,
  DegreeCoursesDisplay,
  DegreeRequest,
  DegreeTitle,
} from "../../features/degree";
import { useAppSelector } from "../../hooks";
import { useParams } from "react-router-dom";
import { AppPaths } from "../../App";
import * as marked from "marked";
import {
  Article,
  ArticleCategory,
  ArticlesDisplay,
} from "../../features/article";
import FullWidthColoredBackground from "../../common_components/Card/FullWidthColoredBackground";

export default function Degree() {
  const { degreeId } = useParams<{ degreeId: string }>();
  const [degree, setDegree] = useState<DegreeType | null>(null);
  const degreesLinks = useAppSelector((state) => state.links.degrees);
  const articlesLinks = useAppSelector((state) => state.links.articles);
  const [articles, setArticles] = useState<Article[]>([]);

  // const degreePageContent: DegreePageContentInterface = require(`./json/degree-${degreeId}.json`);

  useEffect(() => {
    const handleFetchDegree = async () => {
      // Prepare the link
      if (!degreesLinks.getById || !degreeId) {
        return;
      }

      // Call the API
      const response = await DegreeRequest.getById({
        link: degreesLinks.getById,
        id: degreeId,
      });

      // Set the degree
      setDegree(response);
    };

    handleFetchDegree();
  }, [degreesLinks, degreeId]);

  // useEffect(() => {
  //   const handleFetchArticles = async () => {
  //     // Prepare the link
  //     if (!articlesLinks.search) {
  //       return;
  //     }
  //
  //     // Call the api
  //     const response: GetArticlesResponse = await ArticleRequest.getArticles({
  //       link: articlesLinks.search,
  //       category: ArticleCategory.EVENTS,
  //       pagination: { size: 3 },
  //     });
  //
  //     // Set the articles
  //     setArticles(response.articles);
  //   };
  //
  //   handleFetchArticles();
  // }, [articlesLinks]);

  if (!degree) {
    return <span>Loading</span>;
  }

  return (
    <div>
      {/* Top greeting */}
      <Greetings
        imageLink={`${AppPaths.IMAGES}/${degree.imageName}`}
        heading={`${
          degree.title === DegreeTitle.BACHELOR_OF_SCIENCE
            ? "BACHELOR"
            : degree.title
        }'S DEGREE IN ${degree.fieldOfStudy.toUpperCase()}`}
        text={
          "Enroll at Aquila University and acquire the expertise to shape the digital societies of tomorrow."
        }
      />
      <section
        className={"flex flex-col px-4 md:px-32 my-16 gap-8 lg:flex-row"}
      >
        <div
          className={"markdown-paragraph px-4 lg:px-0"}
          dangerouslySetInnerHTML={{
            __html: marked.marked(degree.description),
          }}
        ></div>
        <div className={"flex flex-col h-fit w-full flex-none gap-4 lg:w-1/3"}>
          <DegreeBasicInformation degree={degree} />
          <DegreeCoursesDisplay degree={degree} />
        </div>
      </section>
      <section className={"w-full px-4 md:px-32 my-16"}>
        <ArticlesDisplay
          heading={"Studying at the Aquila University"}
          category={ArticleCategory.EVENTS}
        />
      </section>
      <div className={"w-full px-4 md:px-32 my-16"}>
        <FullWidthColoredBackground
          color={"black"}
          textColor={"white"}
          heading={"ENROLLMENT PROCESS"}
          text={
            "If you have any questions about the enrollment process, don't hesitate to get in touch with our team at Aquila University. We're here to offer you the necessary guidance and support to address any queries you may have."
          }
          buttonText={"Contact us"}
          buttonLink={AppPaths.CONTACT}
        />
      </div>
    </div>
  );
}
