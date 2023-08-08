import { Link } from "react-router-dom";
import { ReactComponent as ArrowRight } from "../../../assets/icons/arrow-right-white.svg";

export interface LinkButtonPrimaryProps {
  text: string;
  link: string;
}

export default function LinkButtonNoBorderOrBackground(
  props: LinkButtonPrimaryProps,
) {
  const { text, link } = props;

  return (
    <Link
      to={link}
      className="flex items-center justify-between gap-3 w-full border-b border-white bg-brandMainSoft text-center text-white font-bold text-md py-2"
    >
      {text} <ArrowRight className="w-6 h-6" />
    </Link>
  );
}
