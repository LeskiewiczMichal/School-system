import GlassBuilding from "../features/main-page/assets/glass-building.webp";
import { ReactComponent as ArrowRight } from "../assets/icons/arrow-right-white.svg";
import { Link } from "react-router-dom";
import LinkButtonPrimary from "./LinkButtonPrimary";

export interface LinkButtonPrimaryProps {
  heading: string;
  text: string;
  buttonLink: string;
  imageLink: string;
}

export default function TextAndButtonWithPhotoOnRight(
  props: LinkButtonPrimaryProps,
) {
  const { heading, text, buttonLink, imageLink } = props;

  return (
    <section className="col-span-3 flex flex-col lg:flex-row lg:pt-14 pb-14">
      <img
        src={imageLink}
        className="w-full lg:w-1/2 lg:order-2"
        alt="School building"
      />
      <div className="flex flex-col lg:justify-center pl-8 pr-8 lg:pl-0 lg:pr-0 gap-4 pt-8 lg:order-1">
        <h2 className="text-5xl font-extrabold text-primary">{heading}</h2>
        <p>{text}</p>
        <LinkButtonPrimary
          text={"Browse the degree catalogue"}
          link={buttonLink}
        />
      </div>
    </section>
  );
}
