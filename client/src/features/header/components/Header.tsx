import { ReactComponent as Logo } from "../../../assets/logo.svg";

export default function Header() {
  return (
    <header className="max-h-16 flex justify-between items-center py-6 pr-8 bg-white border-b">
      <div className="flex w-min-1/3 items-center">
        {/*<img className="h-8 w-8 mr-2" src={Logo} alt="Logo" />*/}
        <Logo className="h-28 w-28" />
        <h1 className="text-2xl w-full font-bold text-primary italic">
          Aquila University
        </h1>
      </div>

      <div className="flex items-center">
        <a
          className="inline-block text-sm font-bold text-primary align-baseline hover:text-primaryLighter"
          href="#"
        >
          Sign In
        </a>
        <a
          className="inline-block text-sm font-bold text-primary align-baseline hover:text-primaryLighter"
          href="#"
        >
          Sign Up
        </a>
      </div>
    </header>
  );
}
