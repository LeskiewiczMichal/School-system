// @ts-ignore
const changePage = (
  direction: "next" | "previous",
  setPageFunction: React.Dispatch<React.SetStateAction<number>>,
) => {
  setPageFunction((prevPage) => {
    if (direction === "next") {
      return prevPage + 1;
    } else {
      return prevPage - 1;
    }
  });
};

export default changePage;
