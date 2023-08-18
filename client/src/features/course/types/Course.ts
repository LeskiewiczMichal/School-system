import APILink from "../../../type/APILink";
import Language from "../../../type/Language";
import CourseScope from "./CourseScope";

export default interface Course {
  id: bigint;
  title: string;
  durationInHours: number;
  language: Language;
  scope: CourseScope[];
  faculty: {
    name: string;
    link: APILink;
  };
  teacher: {
    name: string;
    link: APILink;
  };
  students: APILink;
  files: APILink;
  description: APILink;
}
