import { Link } from "react-router-dom";
import NullableString from "../type/NullableString";

export interface CardProps {
  title: string;
  text: string;
  imageUrl: NullableString;
  imageAlt: string;
  articleId: string;
  buttonText?: string;
  redirectUrl: string;
  wide?: boolean;
}

export default function Card(props: CardProps) {
  const {
    title,
    text,
    imageUrl,
    imageAlt,
    buttonText = "See more",
    articleId,
    redirectUrl,
    wide = false,
  } = props;

  const truncatedText = text.length > 100 ? text.slice(0, 100) + "..." : text;

  return (
    <Link
      to={`${redirectUrl}/${articleId}`}
      className={`flex flex-col w-full  bg-gray-100 border border-gray-200 rounded-lg shadow  hover:bg-grey ${
        wide ? "lg:flex-row items-center" : ""
      }`}
    >
      {imageUrl && (
        <img
          className={`w-full rounded-t-lg h-96 lg:h-auto md:rounded-none md:rounded-l-lg ${
            wide ? "w-1/2" : ""
          }`}
          src={imageUrl}
          alt={imageAlt}
        />
      )}

      <div className="flex flex-col justify-between p-4 h-full">
        <div>
          <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900">
            {title}
          </h5>
          <p className="mb-3 font-normal text-gray-700 ">{truncatedText}</p>
        </div>
        <button
          type="button"
          className="text-primary font-bold text-lg self-end hover:text-primaryDarkened"
        >
          Read more...
        </button>
      </div>
    </Link>
  );
}
