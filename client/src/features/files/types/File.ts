import APILink from "../../../type/APILink";

interface File {
  id: bigint;
  fileName: string;
  fileType: string;
  uploadedBy: string;
  linkToFile: APILink;
}

export default File;
