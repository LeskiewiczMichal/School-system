import { Degree } from "../../features/degree";
import { useEffect, useState } from "react";
import { useAppSelector } from "../../hooks";

export default function DegreeProgrammes() {
  const links = useAppSelector((state) => state.links);
  const [degrees, setDegrees] = useState<Degree[]>([]);

  useEffect(() => {
    const handleFetchDegrees = async () => {
      // Prepare the link
      if (!links.degrees) {
        return;
      }

      // Call the api
    };
  }, [links]);

  return (
    <div>
      <h1>Degree Programmes</h1>
    </div>
  );
}
