import { ReactComponent as Logo } from "../../../assets/logo/logo-white.svg";
import { ReactComponent as Linkedin } from "../../../assets/icons/linkedin.svg";
import LinkButtonNoBorderOrBackground from "./LinkButtonNoBorderOrBackground";

export default function Footer() {
  return (
    <footer className="z-50 flex gap-4 flex-auto flex-col text-white justify-center items-center py-6 px-4 bg-brandMainSoft">
      <section
        className={
          "grid grid-cols-1 sm:grid-cols-2 gap-1 w-full h-full place-content-center justify-center items-center"
        }
      >
        <LinkButtonNoBorderOrBackground
          text={"About the website"}
          link={"/about-us/about-website"}
        />
        <LinkButtonNoBorderOrBackground
          text={"Cookie management"}
          link={"/about-us/about-website"}
        />
        <LinkButtonNoBorderOrBackground
          text={"Give feedback"}
          link={"/about-us/about-website"}
        />
      </section>
      <div className={"mb-4"}>
        <div className={"flex gap-3 items-center"}>
          <Logo className="h-16 w-16" />
          <h4 className={"text font-bold"}>Aquila University</h4>
        </div>
      </div>
      <div className={"flex flex-col gap-2"}>
        <p className={"font-bold"}>Follow us</p>
        <div className={"flex justify-center"}>
          <a href="">
            <Linkedin className={"h-8 w-8"} />
          </a>
        </div>
      </div>
    </footer>
  );
}
