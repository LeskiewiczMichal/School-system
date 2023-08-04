interface PaginationParams {
  url: string;
  templated: boolean;
}

const performGetRequest = (params: PaginationParams) => {
  const { url, templated } = params;

  if (templated) {
  }
};

const RequestService = {
  performGetRequest,
};

export default RequestService;
