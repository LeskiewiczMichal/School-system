import { ReactComponent as Logo } from "../../../assets/logo/logo-white.svg";

export default function Footer() {
  return (
    <footer className="z-50 flex flex-auto text-white justify-between items-center py-6 pr-8 bg-brandMainSoft">
      <div>
        <Logo className="h-28 w-28" />
      </div>
    </footer>
  );
}
