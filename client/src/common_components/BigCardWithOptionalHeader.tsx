import Card from "./Card";

interface BigCardWithOptionalHeaderProps {
  title: string;
  text: string;
}

export default function BigCardWithOptionalHeader(
  props: BigCardWithOptionalHeaderProps,
) {
  const { title, text } = props;

  return (
    <section className="col-span-3">
      <h4 className="mb-8 text-4xl font-bold">{title}</h4>
      <p className="mb-6">{text}</p>

      <Card
        imageUrl="https://img.freepik.com/free-photo/harvard-university-cambridge-usa_1268-14363.jpg"
        imageAlt="building"
        redirectUrl="http://localhost:8080/articles"
        articleId="3"
        title={"Okay"}
        text="lorem ipsum lorem ipsum lorem ipsum lorem ipsum"
        wide
      />
    </section>
  );
}
