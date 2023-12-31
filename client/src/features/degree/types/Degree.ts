import DegreeTitle from "./DegreeTitle";
import APILink from "../../../type/APILink";
import Language from "../../../type/Language";

export default interface Degree {
  id: bigint;
  title: DegreeTitle;
  fieldOfStudy: string;
  lengthOfStudy: number;
  tuitionFeePerYear: number;
  languages: Language[];
  description: string;
  imageName: string | null;
  faculty: {
    name: string;
    link: APILink;
  };
  courses: {
    link: APILink;
  };
}
