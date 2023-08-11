interface MyHeadingWithLineProps {
  heading: string;
}

export default function MyHeadingWithLine(props: MyHeadingWithLineProps) {
  const { heading } = props;

  return (
    <div className={"flex mb-8 w-full items-center"}>
      <h4 className="my-header text-brandMainNearlyBlack">{heading}</h4>
      <div className={"h-[1px] grow bg-black ml-6"}></div>
    </div>
  );
}
