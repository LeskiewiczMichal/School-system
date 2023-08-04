import { ReactComponent as Logo } from "../../../assets/logo.svg";
import FacultiesDropdown from "./FacultiesDropdown";
import { Link } from "react-router-dom";

export default function Header() {
  return (
    <header className="z-50 max-h-20 flex flex-auto justify-between items-center py-6 pr-8 bg-white border-b-2 border-primary">
      <div className="flex  items-center">
        <Link to="/" className="h-28 w-28">
          <Logo className="h-full w-full" />
        </Link>
        <h1 className="text-xl md:text-2xl w-full font-bold text-primary italic">
          Aquila University
        </h1>
      </div>

      <div className="flex items-center  justify-end gap-4">
        <FacultiesDropdown />
        <a
          className="inline-block text-md font-bold text-primary align-baseline hover:text-primaryLighter"
          href="#"
        >
          About
        </a>
        <a
          className="inline-block min-w-fit text-md font-bold text-primary align-baseline hover:text-primaryLighter"
          href="#"
        >
          Sign In
        </a>
      </div>
    </header>
  );
}
