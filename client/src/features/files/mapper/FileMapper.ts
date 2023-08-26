import File from "../types/File";
import Mapper from "../../../type/Mapper";

const mapFromServerData = (data: any): File => {
  return {
    id: data.id,
    fileName: data.fileName,
    fileType: data.fileType,
    uploadedBy: data.uploadedBy,
    linkToFile: {
      href: data._links.self.href,
      templated: data._links.self.templated === true,
    },
  };
};

const mapArrayFromServerData = (data: any[]): File[] => {
  return data.map((file) => mapFromServerData(file));
};

const FileMapper: Mapper<File> = {
  mapFromServerData,
  mapArrayFromServerData,
};

export default FileMapper;
