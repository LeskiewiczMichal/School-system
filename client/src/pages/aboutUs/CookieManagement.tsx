import { Sidebar } from "../../features/sidebar";
import FooterLinksCreator, {
  ActiveFooterPage,
} from "../../features/footer/utils/FooterLinksCreator";

export default function CookieManagement() {
  const links = FooterLinksCreator.createFooterNavigationLinks(
    ActiveFooterPage.COOKIE_MANAGEMENT,
  );

  return (
    <div className={"flex h-full"}>
      <Sidebar links={links} />
      <main className={"h-full w-full flex flex-col py-8 px-8 lg:pr-32"}>
        <h1 className="page-title_h1 px-2 lg:px-0 text-brandMainNearlyBlack">
          COOKIE MANAGEMENT
        </h1>
        <span>
          Welcome to our page's cookie management guide, designed to provide you
          with insight into how we handle cookies and your online experience.
        </span>

        <h2 className={"my-header mb-2 mt-8"}>The Essence of Cookies</h2>
        <span>
          Cookies are small text files that our website stores on your device
          when you visit. These files serve various purposes, such as
          remembering your preferences, enhancing your user experience, and
          providing valuable analytics insights. They're a fundamental tool for
          modern web interactions.
        </span>

        <h2 className={"my-header mb-2 mt-6"}>Our Approach to Cookies</h2>
        <span>
          We value your privacy and transparency. Here's how we manage cookies:
        </span>

        <h2 className={"my-header mb-2 mt-6"}>Setting Cookies</h2>
        <span>
          We use cookies to remember your preferences, such as language
          settings, so you don't have to set them every time you visit. These
          cookies are essential for providing a seamless browsing experience.
        </span>

        <h2 className={"my-header mb-2 mt-6"}>
          Analytics and Performance Cookies
        </h2>
        <span>
          We employ cookies to gather anonymized data about how you interact
          with our website. This helps us understand user behavior, identify
          trends, and optimize our content accordingly. The data collected is
          used solely for improving our website's performance.
        </span>

        <h2 className={"my-header mb-2 mt-6"}>Third-Party Cookies</h2>
        <span>
          Some of our pages may incorporate content from external platforms such
          as social media or embedded videos. These platforms may set their own
          cookies for tracking and analytics purposes. We recommend reviewing
          the respective platforms' privacy policies for more information.
        </span>

        <h2 className={"my-header mb-2 mt-6"}>Managing Your Preferences</h2>
        <span>
          You have the freedom to manage your cookie preferences. Most web
          browsers allow you to control cookies through their settings. You can
          choose to accept or reject cookies, delete specific cookies, or clear
          all stored cookies. Be aware that modifying cookie settings may impact
          your browsing experience on our site.
        </span>

        <h2 className={"my-header mb-2 mt-6"}>Stay Informed</h2>
        <span>
          As technology evolves, so does our cookie management strategy. We'll
          keep you informed about any changes in our approach, ensuring you're
          always aware of how we utilize cookies. We're here to address any
          questions or concerns you may have about cookies. Feel free to reach
          out through our designated feedback channels.
        </span>

        <span className={"mt-6"}>
          <b>Thank you for being a part of our online community.</b> Your trust
          matters to us, and we're committed to making your browsing experience
          as seamless and respectful of your privacy as possible.
        </span>
      </main>
    </div>
  );
}
