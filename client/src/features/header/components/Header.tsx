import { ReactComponent as Logo } from "../../../assets/logo.svg";
import FacultiesDropdown from "./FacultiesDropdown";

export default function Header() {
  return (
    <header className="max-h-16 flex flex-auto justify-between items-center py-6 pr-8 bg-white border-b-2 border-primary">
      <div className="flex  items-center">
        {/*<img className="h-8 w-8 mr-2" src={Logo} alt="Logo" />*/}
        <Logo className="h-28 w-28" />
        <h1 className="text-xl md:text-2xl w-full font-bold text-primary italic">
          Aquila University
        </h1>
      </div>

      <div className="flex items-center  justify-end gap-4">
        {/*<a*/}
        {/*  className="inline-block text-sm font-bold text-primary align-baseline hover:text-primaryLighter"*/}
        {/*  href="#"*/}
        {/*>*/}
        {/*  Faculties*/}
        {/*</a>*/}
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
