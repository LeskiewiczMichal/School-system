import { Link } from "react-router-dom";

export interface DegreeCardProps {
  cardLink: string;
}

export default function DegreeCard(props: DegreeCardProps) {
  const { cardLink } = props;

  return (
    <Link to={cardLink}>
      <div className="flex mb-8"></div>
    </Link>
  );
}
