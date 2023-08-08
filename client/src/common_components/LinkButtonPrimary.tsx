import { ReactComponent as ArrowRight } from "../assets/icons/arrow-right-white.svg";
import { Link } from "react-router-dom";

export interface LinkButtonPrimaryProps {
  text: string;
  link: string;
}

export default function LinkButtonPrimary(props: LinkButtonPrimaryProps) {
  const { text, link } = props;

  return (
    <Link
      to={link}
      className="flex items-center justify-between gap-3 w-3/5 bg-brandMainSoft text-center text-white font-bold text-md py-3 px-4 hover:bg-primaryDarkened"
    >
      {text} <ArrowRight className="w-6 h-6" />
    </Link>
  );
}
