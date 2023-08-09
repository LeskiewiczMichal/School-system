import GlassBuilding from "../../features/main-page/assets/glass-building.webp";
import { ReactComponent as ArrowRight } from "../../assets/icons/arrow/arrow-right-white.svg";
import { Link } from "react-router-dom";
import LinkButtonPrimary from "../button/LinkButtonPrimary";
import LinkButtonBorderOnly from "../button/LinkButtonBorderOnly";

export interface DarkBackgroundWithPhotoOnRightProps {
  heading: string;
  text: string;
  buttonLink: string;
  buttonText: string;
  imageLink: string;
}

export default function DarkBackgroundWithPhotoOnRight(
  props: DarkBackgroundWithPhotoOnRightProps,
) {
  const { heading, text, buttonLink, imageLink, buttonText } = props;

  return (
    <section className="col-span-3 w-full flex flex-col items-center lg:flex-row h-full ">
      <img
        src={imageLink}
        className="w-full  lg:w-1/2 lg:h-80 lg:py-1 lg:order-2"
        alt="School building"
      />
      <div className="px-6 w-full lg:w-1/2 h-full flex flex-col bg-black text-white lg:justify-center py-20 lg:order-1 ">
        <h2 className="text-4xl font-bold text-white mb-2">{heading}</h2>
        <p className={"mb-4"}>{text}</p>
        <LinkButtonBorderOnly
          text={buttonText}
          link={buttonLink}
          color={"white"}
          width={"w-4/5"}
        />
      </div>
    </section>
  );
}
