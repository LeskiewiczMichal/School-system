import { ReactComponent as Logo } from "../../../assets/logo/logo-white.svg";
import { ReactComponent as Linkedin } from "../../../assets/icons/linkedin.svg";
import LinkButtonNoBorderOrBackground from "./LinkButtonNoBorderOrBackground";

export default function Footer() {
  return (
    <footer className="z-50 relative flex gap-4 flex-auto flex-col md:flex-row text-white justify-center items-center py-6 px-4 md:px-12 bg-brandMain">
      <section
        className={
          "grid grid-cols-1 sm:grid-cols-2 gap-x-4 w-full  place-content-center justify-center items-center md:order-2 md:max-w-xl"
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
      <section
        className={
          "flex flex-col items-center gap-6 md:items-start md:order-1 md:w-3/5"
        }
      >
        <div className={""}>
          <div className={"flex gap-3 items-center"}>
            <Logo className="h-16 w-16" />
            <h4 className={"text font-bold"}>Aquila University</h4>
          </div>
        </div>
        <div className={"flex flex-col gap-2"}>
          <p className={"font-bold"}>Follow us</p>
          <div className={"flex justify-center md:justify-start"}>
            <a href="http://www.linkedin.com/in/michał-leśkiewicz-0b964425b">
              <Linkedin className={"h-8 w-8"} />
            </a>
          </div>
        </div>
        <p className={"font-bold"}>© Aquila University 2023</p>
      </section>
    </footer>
  );
}
