import APILink from "../../../type/APILink";

export default interface Course {
  id: bigint;
  title: string;
  durationInHours: number;
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
}
