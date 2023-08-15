import { Link } from "react-router-dom";
import { ReactComponent as ArrowRight } from "../../assets/icons/arrow/arrow-right-white.svg";

export interface GreetingsProps {
  imageLink: string;
  heading: string;
  text: string;
  buttonText?: string;
}

export default function Greetings(props: GreetingsProps) {
  const { imageLink, heading, text, buttonText } = props;

  return (
    <section className="flex flex-col lg:flex-row lg:pt-14 pb-14 border-b border-brandMainLight">
      <img
        src={imageLink}
        className="w-full lg:w-1/2 lg:order-2"
        alt="Faculty greeting"
      />
      <div className="flex flex-col justify-center pl-8 pr-8 lg:pr-16 lg:pl-16 gap-4 pt-8 lg:order-1">
        <h2 className="text-5xl font-extrabold text-brandMain">{heading}</h2>
        <p>{text}</p>
        {buttonText && (
          <Link
            to="/degrees"
            className="flex items-center justify-between bg-brandMain w-3/5 text-center text-white font-bold text-md py-2 px-4 hover:bg-brandMainActive"
          >
            {buttonText} <ArrowRight className={"h-6 w-6"} />
          </Link>
        )}
      </div>
    </section>
  );
}
