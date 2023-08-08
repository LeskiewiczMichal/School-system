import GlassBuilding from "../features/main-page/assets/glass-building.webp";
import { ReactComponent as ArrowRight } from "../assets/icons/arrow-right-white.svg";
import { Link } from "react-router-dom";

export default function TextAndButtonWithPhotoOnRight() {
  return (
    <section className="col-span-3 flex flex-col lg:flex-row lg:pt-14 pb-14 border-b border-primaryLighter">
      <div className="flex flex-col justify-center pl-8 pr-8 lg:pr-16 gap-4 pt-8">
        <h2 className="text-5xl font-extrabold text-primary">
          EMBRACE THE FUTURE AND UNLOCK YOUR POTENTIAL
        </h2>
        <p>
          Welcome to our university's virtual campus, offering an array of
          opportunities for the upcoming academic adventure.
        </p>
        <Link
          to="/degrees"
          className="flex items-center justify-between gap-3 w-2/3 bg-primary text-center text-white font-bold text-md py-3 px-4 hover:bg-primaryDarkened"
        >
          Browse the degree catalogue <ArrowRight className="w-6 h-6" />
        </Link>
      </div>
      <img
        src={GlassBuilding}
        className="w-full lg:w-1/2"
        alt="School building"
      />
    </section>
  );
}
