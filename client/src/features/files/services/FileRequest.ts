import APILink from "../../../type/APILink";
import axios from "axios";

const downloadFile = async (fileLink: APILink) => {
  try {
    const response = await axios.get(fileLink.href, {
      responseType: "blob",
    });

    const blob = new Blob([response.data]);
    const url = window.URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.setAttribute(
      "download",
      response.headers["content-disposition"].split("=")[1],
    );
    document.body.appendChild(link);
    link.click();

    URL.revokeObjectURL(url);
    link.remove();
  } catch (error) {
    console.error("Error downloading file:", error);
  }
};

const FileRequest = {
  downloadFile,
};

export default FileRequest;
