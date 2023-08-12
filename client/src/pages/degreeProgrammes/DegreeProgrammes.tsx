import { Degree, DegreeRequest } from "../../features/degree";
import { useEffect, useState } from "react";
import { useAppSelector } from "../../hooks";
import { GetDegreesResponse } from "../../features/degree/services/DegreeRequest";

export default function DegreeProgrammes() {
  const links = useAppSelector((state) => state.links);
  const [degrees, setDegrees] = useState<Degree[]>([]);
  const [page, setPage] = useState<number>(0);

  useEffect(() => {
    const handleFetchDegrees = async () => {
      // Prepare the link
      if (!links.degrees.getDegrees) {
        return;
      }

      // Call the api
      const response: GetDegreesResponse = await DegreeRequest.getList({
        link: links.degrees.getDegrees,
        pagination: { page: page },
      });

      // TODO: Set the degrees
    };

    handleFetchDegrees();
  }, [links, page]);

  return (
    <div>
      <h1>Degree Programmes</h1>
    </div>
  );
}
