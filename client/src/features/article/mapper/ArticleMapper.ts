import Article from "../Types/Article";

const mapArticleFromServerData = (data: any): Article => {
  return {
    id: data.id,
    title: data.title,
    header: data.header,
    preview: data.preview,
    category: data.category,
    faculty: data.faculty,
    content: data.content,
    author: data.author,
    imgPath: data.imgPath,
  };
};

const mapArticleArrayFromServerData = (data: any[]): Article[] => {
  return data.map((article) => mapArticleFromServerData(article));
};

const ArticleMapper = {
  mapArticleFromServerData,
  mapArticleArrayFromServerData,
};

export default ArticleMapper;
