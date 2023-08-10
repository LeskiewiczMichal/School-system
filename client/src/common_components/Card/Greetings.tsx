import { Link } from "react-router-dom";

export default function Greetings() {
  return (
    <section className="flex flex-col lg:flex-row lg:pt-14 pb-14 border-b border-brandMainLight">
      <img
        // src={GlassBuilding}
        className="w-full lg:w-1/2"
        alt="School building"
      />
      <div className="flex flex-col justify-center pl-8 pr-8 lg:pr-16 gap-4 pt-8">
        <h2 className="text-5xl font-extrabold text-brandMain">
          EMBRACE THE FUTURE AND UNLOCK YOUR POTENTIAL
        </h2>
        <p>
          Welcome to our university's virtual campus, offering an array of
          opportunities for the upcoming academic adventure.
        </p>
        <Link
          to="/degrees"
          className="bg-brandMain text-center text-white font-bold text-md py-2 px-4 hover:bg-brandMainActive"
        >
          Browse the degree catalogue
        </Link>
      </div>
    </section>
  );
}
