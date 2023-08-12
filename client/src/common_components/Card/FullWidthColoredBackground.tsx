import LinkButtonBorderOnly from "../button/LinkButtonBorderOnly";

export interface FullWidthColoredBackgroundProps {
  color: string;
  textColor: string;
  heading: string;
  text: string;
  buttonText: string;
  buttonLink: string;
}

export default function FullWidthColoredBackground(
  props: FullWidthColoredBackgroundProps,
) {
  const { color, textColor, heading, text, buttonText, buttonLink } = props;

  return (
    <div className={`bg-${color} text-${textColor} py-8 px-10`}>
      <h2 className="text-4xl font-bold mb-4">{heading}</h2>
      <p className={"mb-4"}>{text}</p>
      <LinkButtonBorderOnly
        text={buttonText}
        link={buttonLink}
        color={"white"}
        width={"w-full md:w-2/5"}
      />
    </div>
  );
}
