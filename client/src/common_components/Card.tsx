import { Link } from "react-router-dom";

export interface CardProps {
  title: string;
  text: string;
  imageUrl: string;
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

  // return (
  //   <div
  //     className={`flex grow flex-col justify-center items-center bg-grey shadow-lg  min-h-96 w-full ${
  //       wide ? "md:flex-row" : ""
  //     }`}
  //   >
  //     <div className="flex justify-center items-center w-full">
  //       <img src={imageUrl} alt={imageAlt} />
  //     </div>
  //     <div className="flex flex-col justify-center p-4">
  //       <h2 className="text-xl font-medium text-gray-800">{title}</h2>
  //       <p className="text-gray-500 text-base">{truncatedText}</p>
  //     </div>
  //     <div className="flex justify-end items-center w-full p-4">
  //       <Link
  //         to={`${redirectUrl}/${articleId}`}
  //         className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full"
  //       >
  //         {buttonText}
  //       </Link>
  //     </div>
  //   </div>
  // );

  return (
    <a
      href="#"
      className={`flex flex-col w-full items-center bg-white border border-gray-200 rounded-lg shadow  hover:bg-gray-100 ${
        wide ? "md:flex-row" : ""
      }`}
    >
      <img
        className=" w-full rounded-t-lg h-96 md:h-auto md:rounded-none md:rounded-l-lg"
        src={imageUrl}
        alt=""
      />
      <div className="flex flex-col justify-between p-4 ">
        <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900">
          Noteworthy technology acquisitions 2021
        </h5>
        <p className="mb-3 font-normal text-gray-700 ">
          Here are the biggest enterprise technology acquisitions of 2021 so
          far, in reverse chronological order. Here are the biggest enterprise
          technology acquisitions of 2021 so far, in reverse chronological
          order. Here are the biggest enterprise technology acquisitions of 2021
          so far, in reverse chronological order.
        </p>
      </div>
    </a>
  );
}
