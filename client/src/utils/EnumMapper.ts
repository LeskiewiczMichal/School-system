import { DegreeTitle } from "../features/degree";
import Language from "../type/Language";
import Role from "../type/Role";

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

const mapLanguageToString = (language: Language): string => {
  switch (language) {
    case Language.ENGLISH:
      return "English";
    case Language.POLISH:
      return "German";
    default:
      return "Unknown";
  }
};

const mapRoleToString = (role: Role): string => {
  switch (role) {
    case Role.ADMIN:
      return "Admin";
    case Role.STUDENT:
      return "Student";
    case Role.TEACHER:
      return "Teacher";
    default:
      return "Unknown";
  }
};

const EnumMapper = {
  mapDegreeTitleToString,
  mapLanguageToString,
  mapRoleToString,
};

export default EnumMapper;
