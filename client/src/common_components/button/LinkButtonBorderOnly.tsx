import { Link } from "react-router-dom";
import { ReactComponent as ArrowRightWhite } from "../../assets/icons/arrow/arrow-right-white.svg";
import { ReactComponent as ArrowRightBlack } from "../../assets/icons/arrow/arrow-right.svg";
import { ReactComponent as ArrowRightBrandMain } from "../../assets/icons/arrow/arrow-right-primary.svg";

interface LinkButtonBorderOnlyProps {
  text: string;
  link: string;
  color: "white" | "black" | "brandMain";
}

export default function LinkButtonBorderOnly(props: LinkButtonBorderOnlyProps) {
  const { text, link, color } = props;

  return (
    <Link
      to={link}
      type="button"
      className={`mb-16 w-3/5 flex items-center border-4 border-${color} gap-3 px-4 py-2 text-${color} ${
        color === "brandMain"
          ? "hover:border-brandMainActive hover:text-brandMainActive"
          : ""
      }  font-bold text-lg`}
    >
      {text} {color === "white" && <ArrowRightWhite className="w-6 h-6 " />}
      {color === "black" && <ArrowRightBlack className="w-6 h-6 " />}
      {color === "brandMain" && <ArrowRightBrandMain className="w-6 h-6 " />}
    </Link>
  );
}
