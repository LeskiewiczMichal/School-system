import NullableString from "../../../type/NullableString";
import ArticleCategory from "./ArticleCategory";
import APILink from "../../../type/APILink";

interface Article {
  id: bigint;
  title: string;
  header: string;
  preview: string;
  category: ArticleCategory;
  faculty: {
    name: string;
    link: APILink;
  } | null;
  content: NullableString;
  author: {
    name: string;
    link: APILink;
  } | null;
  imgPath: NullableString;
}

export default Article;
