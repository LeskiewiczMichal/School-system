import ArticleCategory from "../Types/ArticleCategory";

export interface ArticlesSidebarProps {
  category: ArticleCategory;
}

export default function ArticlesSidebar(props: ArticlesSidebarProps) {
  const { category } = props;

  return (
    <div className="flex flex-col z-40 flex-none gap-6 bg-grayscaleLight w-56 mt-8 xl:order-2 xl:mt-0 py-4 px-8 xl:px-0 xl:max-h-screen xl:sticky xl:top-0">
      <div className={"flex flex-col text-brandMain font-bold gap-4"}>
        <span className={""}>Aquila University</span>
        <span className={"bg-brandMain text-white w-fit px-2 py-1"}>
          {category}
        </span>
      </div>
    </div>
  );
}
