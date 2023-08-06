import NullableString from "../../../type/NullableString";
import ArticleCategory from "./ArticleCategory";

interface Article {
  id: bigint;
  title: string;
  header: string;
  preview: string;
  category: ArticleCategory;
  faculty: NullableString;
  content: NullableString;
  author: NullableString;
  imgPath: NullableString;
}

export default Article;
