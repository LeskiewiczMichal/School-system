import Greetings from "../../common_components/Card/Greetings";
import { ReactComponent as Clock } from "../../assets/icons/clock-grey.svg";
import { ReactComponent as Dollar } from "../../assets/icons/dollar-gray.svg";
import { ReactComponent as Atom } from "../../assets/icons/atom-gray.svg";
import { ReactComponent as LanguageIcon } from "../../assets/icons/language-gray.svg";
import { useEffect, useState } from "react";
import { Degree as DegreeType, DegreeRequest } from "../../features/degree";
import { useAppSelector } from "../../hooks";
import { useParams } from "react-router-dom";
import { AppPaths } from "../../App";
import DegreePageContentInterface from "./DegreePageContentInterface";
import * as marked from "marked";
import EnumMapper from "../../utils/EnumMapper";
import Language from "../../type/Language";
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

  const degreePageContent: DegreePageContentInterface = require(`./json/degree-${degreeId}.json`);

  useEffect(() => {
    const handleFetchDegree = async () => {
      // Prepare the link
      if (!degreesLinks.getById || !degreeId) {
        return;
      }

      // Call the API
      const response = await DegreeRequest.getSingle({
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

  const degreeLevel = EnumMapper.mapDegreeTitleToString(degree.title);
  const languages = degree.languages.map((language: Language) => {
    return EnumMapper.mapLanguageToString(language);
  });

  return (
    <div>
      {/* Top greeting */}
      <Greetings
        imageLink={`${AppPaths.IMAGES}/${degree.imageName}`}
        heading={degreePageContent.greetingsHeading}
        text={degreePageContent.greetingsText}
      />
      <section
        className={"flex flex-col px-4 md:px-32 my-16 gap-8 lg:flex-row"}
      >
        <div
          className={"px-4 lg:px-0"}
          dangerouslySetInnerHTML={{
            __html: marked.marked(degree.description),
          }}
        ></div>
        <div
          className={
            "bg-hoverGray flex flex-col h-fit py-4 px-4 w-full flex-none gap-4 lg:w-1/3"
          }
        >
          <h5 className={"text-2xl text-brandMainNearlyBlack"}>
            Basic information
          </h5>
          <div className={"flex items-center gap-4"}>
            <Clock className={"h-8 w-8"} />
            <div className={"flex flex-col text-grayscaleDark"}>
              <span>Length of studies</span>
              <span className={"font-bold"}>{degree.lengthOfStudy} years</span>
            </div>
          </div>
          <div className={"flex items-center gap-4"}>
            <Atom className={"h-8 w-8"} />
            <div className={"flex flex-col text-grayscaleDark"}>
              <span>Degree level</span>
              <span className={"font-bold"}>{degreeLevel}</span>
            </div>
          </div>
          <div className={"flex items-center gap-4"}>
            <LanguageIcon className={"h-8 w-8"} />
            <div className={"flex flex-col text-grayscaleDark"}>
              <span>Languages</span>
              <span className={"font-bold"}>{languages.join(", ")}</span>
            </div>
          </div>
          <div className={"flex items-center gap-4"}>
            <Dollar className={"h-8 w-8"} />
            <div className={"flex flex-col text-grayscaleDark"}>
              <span>Tuition fee per year</span>
              <span className={"font-bold"}>{degree.tuitionFeePerYear}</span>
            </div>
          </div>
        </div>
      </section>
      <section className={"w-full px-4 md:px-32 my-16"}>
        // TODO: CHANGE ARTICLE CATEGORY TO EVENTS
        <ArticlesDisplay
          heading={"Studying at the Aquila University"}
          category={ArticleCategory.NEWS}
        />
      </section>
      <div className={"w-full px-4 md:px-32 my-16"}>
        <FullWidthColoredBackground
          color={"black"}
          textColor={"white"}
          heading={"ADMISSION ASSISTANCE"}
          text={
            "Feel free to reach out to our Admission Assistance team at the University of Helsinki if you have any inquiries regarding the admissions procedure. We're here to provide you with the guidance and assistance you need."
          }
          buttonText={"Contact us"}
          buttonLink={AppPaths.CONTACT}
        />
      </div>
    </div>
  );
}
