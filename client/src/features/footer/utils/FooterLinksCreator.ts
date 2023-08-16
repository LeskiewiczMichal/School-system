import { SidebarLinkProps } from "../../sidebar/components/SidebarLink";

export enum ActiveFooterPage {
  ABOUT_WEBSITE,
  CONTACT,
  COOKIE_MANAGEMENT,
}

const createFooterNavigationLinks = (
  active: ActiveFooterPage,
): SidebarLinkProps[] => {
  return [
    {
      title: "About website",
      redirectUrl: `/about-us/about-website`,
      active: active === ActiveFooterPage.ABOUT_WEBSITE,
    },
    {
      title: "Cookie management",
      redirectUrl: `/about-us/cookie-management`,
      active: active === ActiveFooterPage.COOKIE_MANAGEMENT,
    },
    {
      title: "Contact",
      redirectUrl: `/about-us/contact`,
      active: active === ActiveFooterPage.CONTACT,
    },
  ];
};

const FooterLinksCreator = {
  createFooterNavigationLinks,
};

export default FooterLinksCreator;
