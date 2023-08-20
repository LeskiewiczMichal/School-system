import { ReactComponent as ArrowRight } from "../../assets/icons/arrow/arrow-right-white.svg";
import { Link } from "react-router-dom";

export interface LinkButtonPrimaryProps {
  text: string;
  link: string;
  fullWidthOnSmallerScreen?: boolean;
}

export default function LinkButtonPrimary(props: LinkButtonPrimaryProps) {
  const { text, link, fullWidthOnSmallerScreen } = props;

  return (
    <Link
      to={link}
      className={`flex items-center justify-between gap-3 bg-brandMain text-center text-white font-bold text-md py-3 px-4 hover:bg-primaryDarkened ${
        fullWidthOnSmallerScreen ? "w-full lg:w-3/5" : "w-3/5"
      }`}
    >
      {text} <ArrowRight className="w-6 h-6" />
    </Link>
  );
}
