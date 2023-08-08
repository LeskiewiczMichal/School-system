import Card from "./Card";
import { Article } from "../features/article";

interface BigCardWithOptionalHeaderProps {
  title: string;
  text: string;
  article: Article;
}

export default function BigCardWithOptionalHeader(
  props: BigCardWithOptionalHeaderProps,
) {
  const { title, text, article } = props;

  return (
    <section className="col-span-3">
      <h4 className="mb-8 text-4xl font-bold">{title}</h4>
      <p className="mb-6">{text}</p>

      <Card
        imageUrl={article.imgPath}
        imageAlt="Article Preview"
        redirectUrl={`http://localhost:8080/articles/${article.id}`}
        articleId={article.id.toString()}
        title={article.title}
        text={article.preview}
        wide
      />
    </section>
  );
}
