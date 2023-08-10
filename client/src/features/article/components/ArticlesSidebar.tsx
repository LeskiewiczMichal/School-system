import ArticleCategory from "../Types/ArticleCategory";
import {
  FacebookShareButton,
  FacebookIcon,
  TwitterShareButton,
  TwitterIcon,
} from "react-share";

export interface ArticlesSidebarProps {
  category: ArticleCategory;
}

export default function ArticlesSidebar(props: ArticlesSidebarProps) {
  const { category } = props;

  return (
    <div className="flex xl:flex-col z-40 flex-none gap-6 bg-grayscaleLight w-56 mt-8 xl:order-2 xl:mt-0 py-4 px-8 xl:px-0 xl:max-h-screen xl:sticky xl:top-0">
      <div className={"flex flex-col text-brandMain font-bold gap-4"}>
        <span className={""}>Aquila University</span>
        <span className={"bg-brandMain text-white w-fit px-2 py-1"}>
          {category}
        </span>
      </div>
      <div className={"flex flex-col text-brandMain font-bold gap-4 xl:mt-6"}>
        <span>Share this page</span>
        {/*    TODO: SHARE BUTTONS*/}
        <div className={"flex"}>
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
        </div>
      </div>
    </div>
  );
}
