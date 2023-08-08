import { ReactComponent as ChevronRight } from "../../assets/icons/chevron/chevron-right-bright.svg";

export interface SidebarLinkProps {
  active?: boolean;
  title: string;
  redirectUrl?: string;
}

export default function SidebarLink(props: SidebarLinkProps) {
  const { active = false, title, redirectUrl } = props;

  if (active) {
    return (
      <span
        className={
          "text-xl pl-4 py-4 font-bold text-brandMainNearlyBlack border-l-4 border-brandMainNearlyBlack"
        }
      >
        {title}
      </span>
    );
  }

  return (
    <a
      href={redirectUrl}
      className={
        "flex items-center gap-4 text-lg font-bold text-brandMainBright hover:underline"
      }
    >
      <ChevronRight className={"h-6 w-6"} /> {title}
    </a>
  );
}
