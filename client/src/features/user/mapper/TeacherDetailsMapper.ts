import TeacherDetails from "../types/TeacherDetails";

const mapFromServerData = (data: any): TeacherDetails => {
  return {
    id: data.id,
    title: data.title,
    degreeField: data.degreeField,
    bio: data.bio,
    tutorship: data.tutorship,
    teacher: {
      href: data._links.teacher.href,
      templated: false,
    },
  };
};

const TeacherDetailsMapper = {
  mapFromServerData,
};

export default TeacherDetailsMapper;
