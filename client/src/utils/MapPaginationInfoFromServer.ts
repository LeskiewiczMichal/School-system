const mapPaginationInfoFromServer = (serverData: any) => {
  return {
    page: serverData.page.number,
    size: serverData.page.size,
    totalElements: serverData.page.totalElements,
    totalPages: serverData.page.totalPages,
  };
};

export default mapPaginationInfoFromServer;
