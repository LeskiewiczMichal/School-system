import DegreeTitle from "./DegreeTitle";
import APILink from "../../../type/APILink";

export default interface Degree {
  id: bigint;
  title: DegreeTitle;
  fieldOfStudy: string;
  lengthOfStudy: number;
  tuitionFeePerYear: number;
  languages: string[];
  description: string;
  faculty: {
    name: string;
    link: APILink;
  };
  courses: {
    link: APILink;
  };
}
