import LinkButtonBorderOnly from "./button/LinkButtonBorderOnly";

export interface HaveToBeLoggedInInfoProps {
  text?: string;
  button?: {
    text: string;
    link: string;
    color: "brandMain" | "black" | "white";
  };
}

export default function HaveToBeLoggedInInfo(props: HaveToBeLoggedInInfoProps) {
  const { text, button } = props;

  return (
    <div
      className={"h-screen w-screen flex flex-col justify-center items-center"}
    >
      <h1 className={"page-title_h1"}>
        {text ? text : "You have to be logged in to see this page"}
      </h1>
      {button && (
        <LinkButtonBorderOnly
          text={button.text}
          link={button.link}
          color={button.color}
        />
      )}
    </div>
  );
}
