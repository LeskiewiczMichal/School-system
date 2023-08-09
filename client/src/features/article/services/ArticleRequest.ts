import APILink from "../../../type/APILink";
import RequestService from "../../../utils/RequestService";
import {
  OptionalPaginationParams,
  PaginationParams,
  SortDirection,
} from "../../../type/PaginationParams";
import Article from "../Types/Article";
import ArticleMapper from "../mapper/ArticleMapper";
import ArticleCategory from "../Types/ArticleCategory";
import mapPaginationInfoFromServer from "../../../utils/MapPaginationInfoFromServer";
import PaginationInfo from "../../../type/PaginationInfo";

export type FetchArticlesProps = {
  link: APILink;
  faculty?: string;
  category?: ArticleCategory;
  pagination?: OptionalPaginationParams;
};

export interface GetArticlesResponse {
  articles: Article[];
  paginationInfo: PaginationInfo;
}

/**
 * Fetch articles from the API (using search or not)
 *
 * @param props {@link FetchArticlesProps} object,
 * containing the link {@link APILink} - either a search link or a link to all articles,
 * faculty id (optional),
 * category {@link ArticleCategory} (optional) and pagination {@link OptionalPaginationParams} (optional)
 * @returns Promise of an array of {@link Article} objects
 */
const getArticles = async (
  props: FetchArticlesProps,
): Promise<GetArticlesResponse> => {
  // Prepare the link
  const { link, faculty, category, pagination } = props;

  // Prepare the search parameters
  let params = {};
  if (faculty) {
    params = {
      ...params,
      faculty: faculty,
    };
  }
  if (category) {
    params = {
      ...params,
      category: category,
    };
  }

  // Prepare the pagination
  let paginationParams: PaginationParams = {
    page: 0,
    size: 10,
    sort: ["id", SortDirection.DESC],
  };
  if (pagination) {
    paginationParams = {
      page: pagination.page ? pagination.page : paginationParams.page,
      size: pagination.size ? pagination.size : paginationParams.size,
      sort: pagination.sort ? pagination.sort : paginationParams.sort,
    };
  }

  const responseData = await RequestService.performGetRequest({
    link: link,
    pagination: paginationParams,
    params: params,
  });

  // Convert the response data into articles
  const articlesArr: Article[] = ArticleMapper.mapArticleArrayFromServerData(
    responseData._embedded.articles,
  );

  const paginationInfo: PaginationInfo =
    mapPaginationInfoFromServer(responseData);
  return {
    articles: articlesArr,
    paginationInfo: paginationInfo,
  };
};

const ArticleRequest = {
  getArticles,
};

export default ArticleRequest;
