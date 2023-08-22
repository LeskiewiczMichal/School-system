import { DegreeTitle } from "../../degree";
import APILink from "../../../type/APILink";

interface TeacherDetails {
  id: bigint;
  title: DegreeTitle;
  degreeField: string;
  bio: string;
  tutorship: string;
  teacher: APILink;
}

export default TeacherDetails;
