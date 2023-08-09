interface MyHeadingProps {
  heading: string;
}

export default function MyHeading(props: MyHeadingProps) {
  const { heading } = props;

  return <h4 className="my-header text-brandMainNearlyBlack">{heading}</h4>;
}
