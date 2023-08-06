import Card from "../../../common_components/Card";

interface ArticlesDisplayProps {
  heading: string;
}

export default function ArticlesDisplay(props: ArticlesDisplayProps) {
  const { heading } = props;

  return (
    <section className="flex flex-col">
      <h4 className="mb-6 text-4xl font-bold">{heading}</h4>
      <div className="grid grid-cols-1 lg:grid-cols-3  gap-4">
        <Card
          imageUrl="https://img.freepik.com/free-photo/harvard-university-cambridge-usa_1268-14363.jpg"
          imageAlt="building"
          redirectUrl="http://localhost:8080/articles"
          articleId="3"
          title={"Okay"}
          text="lorem ipsum lorem ipsum lorem ipsum lorem ipsum"
        />
        <Card
          imageUrl="https://img.freepik.com/free-photo/harvard-university-cambridge-usa_1268-14363.jpg"
          imageAlt="building"
          redirectUrl="http://localhost:8080/articles"
          articleId="3"
          title={"Okay"}
          text="lorem ipsum lorem ipsum lorem ipsum lorem ipsum"
        />
        <Card
          imageUrl="https://img.freepik.com/free-photo/harvard-university-cambridge-usa_1268-14363.jpg"
          imageAlt="building"
          redirectUrl="http://localhost:8080/articles"
          articleId="3"
          title={"Okay"}
          text="lorem ipsum lorem ipsum lorem ipsum lorem ipsum em ipsum lorem ipsum lorem ipsum lorem ipsum em ipsum lorem ipsum lorem ipsum lorem ipsum"
        />
      </div>
    </section>
  );
}
