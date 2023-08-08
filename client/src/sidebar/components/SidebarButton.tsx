import { ReactComponent as ChevronRight } from "../../assets/icons/chevron/chevron-right-bright.svg";

export interface SidebarButtonProps {
  active?: boolean;
  text: string;
  onClick: () => void;
}

export default function SidebarButton(props: SidebarButtonProps) {
  const { active = false, text, onClick } = props;

  if (active) {
    return (
      <span
        className={
          "text-xl pl-4 py-4 font-bold text-brandMainNearlyBlack border-l-4 border-brandMainNearlyBlack"
        }
      >
        {text}
      </span>
    );
  }

  return (
    <button
      onClick={() => onClick()}
      className={
        "flex items-center gap-4 text-lg font-bold text-brandMainBright hover:underline"
      }
    >
      <ChevronRight className={"h-6 w-6"} /> {text}
    </button>
  );
}
