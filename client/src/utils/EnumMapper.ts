import { DegreeTitle } from "../features/degree";

const mapDegreeTitleToString = (degreeTitle: DegreeTitle): string => {
  switch (degreeTitle) {
    case DegreeTitle.BACHELOR:
      return "Bachelor";
    case DegreeTitle.BACHELOR_OF_SCIENCE:
      return "Bachelor of Science";
    case DegreeTitle.MASTER:
      return "Master";
    case DegreeTitle.DOCTOR:
      return "Doctor";
    case DegreeTitle.PROFESSOR:
      return "Professor";
    default:
      return "Unknown";
  }
};
const EnumMapper = { mapDegreeTitleToString };

export default EnumMapper;
