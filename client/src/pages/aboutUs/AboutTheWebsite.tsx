import { Sidebar } from "../../features/sidebar";
import FooterLinksCreator, {
  ActiveFooterPage,
} from "../../features/footer/utils/FooterLinksCreator";

export default function AboutTheWebsite() {
  const links = FooterLinksCreator.createFooterNavigationLinks(
    ActiveFooterPage.ABOUT_WEBSITE,
  );

  return (
    <div className={"flex h-full"}>
      <Sidebar links={links} />
      <main className={"h-full w-full flex flex-col py-8 px-8 lg:pr-32"}>
        <h1 className="page-title_h1 px-2 lg:px-0 text-brandMainNearlyBlack">
          ABOUT THE WEBSITE
        </h1>
        <span>
          Welcome to the digital hub of the Aquila University. Our website is a
          comprehensive source of information about a diverse array of academic
          offerings and opportunities.
        </span>

        <h2 className={"my-header mb-2 mt-8"}>Discover Diverse Pathways</h2>
        <span>
          We proudly present details on our degree programs, as well as avenues
          for pursuing doctoral studies and lifelong learning experiences.
        </span>

        <h2 className={"my-header mb-2 mt-6"}>A Nexus of Knowledge</h2>
        <span>
          Navigate through the site to find a wealth of research insights, the
          latest news updates, and prospects for collaborative engagement in the
          realm of research and science. Delve into the strategic compass of the
          University, explore available positions, and access the web domains of
          faculties, research collectives, and other integral entities.
        </span>

        <h2 className={"my-header mb-2 mt-6"}>Your Insights, Our Progress</h2>
        <span>
          We wholeheartedly embrace user feedback as an instrument for
          evolution. Queries and comments from users drive our endeavors. Our
          ongoing redesign and development efforts are sculpted by your insights
          and experiences.
        </span>

        <h2 className={"my-header mb-2 mt-6"}>A Glimpse into Our Focus</h2>
        <span>
          The content on the Aquila University's website is a treasure trove
          guarded by copyright. The University’s name and distinctive traits are
          officially trademarked. All rights are reserved, unless explicitly
          stated otherwise.
        </span>

        <h2 className={"my-header mb-2 mt-6"}>Embark on Your Exploration</h2>
        <span className={"font-bold"}>
          Our website serves as a gateway to a spectrum of content:
        </span>

        <ul className={"list-disc ml-6 mb-2"}>
          <li>Degree programs</li>
          <li>Lifelong learning avenues</li>
          <li>Illuminating research</li>
          <li>Timely news updates</li>
          <li>Event summaries</li>
          <li>In-depth insights about the University</li>
          <li>Faculty web domains and associated units</li>
        </ul>

        <span>
          As you embark on your digital journey, we encourage you to engage,
          explore, and enrich your understanding through Aquila's website.
        </span>
      </main>
    </div>
  );
}
