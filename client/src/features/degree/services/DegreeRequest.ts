import { OptionalPaginationParams } from "../../../type/PaginationParams";
import { ArticleCategory } from "../../article";
import APILink from "../../../type/APILink";

export type FetchDegreesProps = {
  link: APILink;
  faculty?: string;
  title?: string;
  pagination?: OptionalPaginationParams;
};
