import Article from "../Types/Article";

const mapArticleFromServerData = (data: any): Article => {
  return {
    id: data.id,
    title: data.title,
    header: data.header,
    preview: data.preview,
    category: data.category,
    faculty: {
      name: data.faculty,
      link: {
        href: data._links.faculty.href,
        templated: data._links.faculty.templated === true,
      },
    },
    content: data.content,
    author: {
      name: data.author,
      link: {
        href: data._links.author.href,
        templated: data._links.author.templated === true,
      },
    },
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
