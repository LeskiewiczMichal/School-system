import ArticleCategory from "../Types/ArticleCategory";
import {
  FacebookShareButton,
  FacebookIcon,
  TwitterShareButton,
  TwitterIcon,
  LinkedinShareButton,
  LinkedinIcon,
} from "react-share";

export interface ArticlesSidebarProps {
  category: ArticleCategory;
}

export default function ArticlesSidebar(props: ArticlesSidebarProps) {
  const { category } = props;

  return (
    <div
      className={
        "flex justify-between xl:justify-start xl:flex-col z-40 flex-none gap-6 border-b border-t border-grayscaleMediumDark xl:border-b-0 xl:border-t-0 bg-grayscaleLight w-full xl:w-56 mt-8 xl:order-2 xl:mt-0 py-8 xl:py-4 px-8 xl:px-0 xl:max-h-screen xl:sticky xl:top-0 mb-8 xl:mb-0"
      }
    >
      <div className={"flex flex-col text-brandMain font-bold gap-4"}>
        <span className={""}>Aquila University</span>
        <span className={"bg-brandMain text-white w-fit px-2 py-1"}>
          {category}
        </span>
      </div>
      <div className={"flex flex-col text-brandMain font-bold gap-2 xl:mt-6"}>
        <span>Share this page</span>
        <div className={"flex gap-4"}>
          <FacebookShareButton
            quote={"Check out this article from the Aquila University"}
            hashtag={"#AquilaUniversity"}
            url={window.location.href}
          >
            <FacebookIcon className={"w-10  h-10 rounded"} />
          </FacebookShareButton>
          <TwitterShareButton
            title={"Check out this article from the Aquila University"}
            hashtags={["#AquilaUniversity"]}
            url={window.location.href}
          >
            <TwitterIcon className={"w-10  h-10 rounded"} />
          </TwitterShareButton>
          <LinkedinShareButton
            title={"Aquila University's page"}
            summary={"Check out this article from the Aquila University"}
            source={window.location.href.toString()}
            url={window.location.href}
          >
            <LinkedinIcon className={"w-9  h-9 rounded"} />
          </LinkedinShareButton>
        </div>
      </div>
    </div>
  );
}
