import GlassBuilding from "../features/main-page/assets/glass-building.webp";
import { ReactComponent as ArrowRight } from "../assets/icons/arrow-right-white.svg";
import { Link } from "react-router-dom";
import LinkButtonPrimary from "./LinkButtonPrimary";

export interface LinkButtonPrimaryProps {
  heading: string;
  text: string;
  buttonLink: string;
  buttonText: string;
  imageLink: string;
}

export default function TextAndButtonWithPhotoOnRight(
  props: LinkButtonPrimaryProps,
) {
  const { heading, text, buttonLink, imageLink, buttonText } = props;

  return (
    <section className="col-span-3 flex flex-col lg:flex-row ">
      <img
        src={imageLink}
        className="w-full lg:w-1/2 lg:order-2"
        alt="School building"
      />
      <div className="flex flex-col lg:justify-center gap-4 pt-8 lg:pt-0 lg:order-1 lg:mr-5">
        <h2 className="text-4xl font-bold text-brandMainNearlyBlack">
          {heading}
        </h2>
        <p>{text}</p>
        <LinkButtonPrimary text={buttonText} link={buttonLink} />
      </div>
    </section>
  );
}
