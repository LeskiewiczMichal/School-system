import { Sidebar } from "../../features/sidebar";
import FooterLinksCreator, {
  ActiveFooterPage,
} from "../../features/footer/utils/FooterLinksCreator";

export default function ContactPage() {
  const links = FooterLinksCreator.createFooterNavigationLinks(
    ActiveFooterPage.CONTACT,
  );

  return (
    <div className={"flex h-full"}>
      <Sidebar links={links} />
      <main className={"pb-16 w-full py-6"}>
        <h1 className="page-title_h1 px-4 lg:px-8 text-brandMainNearlyBlack">
          CONTACT US
        </h1>

        <section className={"flex flex-col gap-2 px-4 lg:px-8"}>
          <span>
            <b>Email: </b>leskiewicz02@gmail.com
          </span>
          <a
            className={" underline"}
            href="https://linkedin.com/in/michał-leśkiewicz-0b964425b"
          >
            Go to linkedin
          </a>
        </section>

        <form></form>
      </main>
    </div>
  );
}
